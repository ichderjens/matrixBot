import de.jojii.matrixclientserver.Bot.Client;
import de.jojii.matrixclientserver.Bot.Events.RoomEvent;
import de.jojii.matrixclientserver.Bot.Member;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

public class ExampleBot {
    public static void main(String[] args) throws IOException {
        //String hostName = "https://matrix.org";
        String hostName = "http://vscode:8008";
        String token = "syt_bXl0ZXN0MjAyMg_xlYVrCQXGpFGCBAlUzQp_0LNUty";
      //  String userName = "mytest2022";
      //  String password = "Qaywsxedc123!";
        String userName = "testdoktor1";
        String password = "Qaywsx123!";
        HashSet<String> alreadyJoinedRoomIDs = new HashSet<>();
        HashSet<String> alreadyConsumedEventIDs = new HashSet<>();
        String inviteUserName = "@testdoktor2022:matrix.org";
        Client matrixClient = new Client(hostName);

        matrixClient.login(userName, password, loginData -> {
            if (loginData.isSuccess()) {
                System.out.println("Login erfolgreich");
                System.out.println("Token:" + matrixClient.getLoginData().getAccess_token());
                matrixClient.registerRoomEventListener(roomEvents -> {
                    for (RoomEvent event : roomEvents) {
                        System.out.println("event:" + event);
                        System.out.println("raw:" + event.getRaw());
                        System.out.println("content:" + event.getContent());
                        if (!alreadyConsumedEventIDs.contains(event.getEvent_id())) {
                            if (event.getType().equals("m.room.member") && event.getContent().has("membership") && event.getContent().getString("membership").equals("invite") && event.getContent().getString("displayname").equals("TestKardiologie(ChatBot)")) {
                                try {
                                    if (!alreadyJoinedRoomIDs.contains(event.getEvent_id())) {
                                        alreadyJoinedRoomIDs.add(event.getEvent_id());
                                        System.out.println("Trete dem Raum bei! : " + event.getEvent_id());
                                        matrixClient.joinRoom(event.getRoom_id(), joined -> {
                                            System.out.println("Ermittle den Usernamen.");
                                            matrixClient.getRoomMembers(event.getRoom_id(), roomMembers -> {
                                                for (Member roomMember : roomMembers) {
                                                    System.out.println("member:" + roomMember.getId());
                                                    if (!roomMember.getDisplay_name().contains("Kardiologie")) {
                                                        matrixClient.sendText(event.getRoom_id(), "Hallo " + roomMember.getDisplay_name(), question -> {
                                                            System.out.println("data: " + question);
                                                            matrixClient.sendText(event.getRoom_id(), "Wählen Sie bitte (T)erminvereinbarung oder (K)onsultation eines Arztes.", null);
                                                        });
                                                    }
                                                }
                                            });
                                        });
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else if (event.getType().equals("m.room.member") && event.getContent().getString("membership").equals("join") && event.getSender().contains(inviteUserName)) {
                                matrixClient.sendText(event.getRoom_id(), "Dr. Test ist nun beigetreten, ich verabschiede mich.", resp -> {
                                    matrixClient.leaveRoom(event.getRoom_id(), null);
                                });
                            } else if (event.getType().equals("m.room.message") && !event.getSender().contains("@mytest2022:matrix.org")) {
                                //Sends a readreceipt  for every received message
                                System.out.println("Antworte auf die Nachricht!");
                                try {
                                    matrixClient.sendReadReceipt(event.getRoom_id(), event.getEvent_id(), "m.read", null);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                if (event.getType().equals("m.room.message") && event.getContent().has("body")) {
                                    String msg = RoomEvent.getBodyFromMessageEvent(event);

                                    if (msg != null && msg.trim().length() > 0) {
                                        switch (msg.toUpperCase()) {
                                            case "T":
                                                matrixClient.sendText(event.getRoom_id(), "Ich kann Ihnen folgenden Termin anbieten: 07.07.2022 15 Uhr. Wollen Sie diesen Termin buchen? (J)a oder (N)ein.", null);
                                                break;
                                            case "K":
                                                matrixClient.sendText(event.getRoom_id(), "Ich lade den diensthabenden Arzt in den Chat ein.", null);
                                                JSONObject invite = new JSONObject();
                                                invite.put("reason", "Wunsch nach Konsultation eines Arztes.");
                                                invite.put("user_id", inviteUserName);
                                                System.out.println(invite);
                                                matrixClient.inviteUser(event.getRoom_id(), invite, data -> {
                                                    matrixClient.sendText(event.getRoom_id(), "Aktuell ist Dr. Test im Dienst, ich habe ihn in den Chat eingeladen.", null);
                                                });
                                                matrixClient.isUserOnline(inviteUserName, null, data -> {
                                                    System.out.println("Online Status" + data);
                                                });

                                                break;
                                            case "J":
                                                matrixClient.sendText(event.getRoom_id(), "Der Termin ist für Sie reserviert. Kann ich Ihnen noch weiterhelfen mit (T)erminvereinbarung oder (K)onsultation eines Arztes?", null);
                                                break;
                                            default:
                                                matrixClient.sendText(event.getRoom_id(), "Wählen Sie bitte (T)erminvereinbarung oder (K)onsultation eines Arztes.", null);
                                                break;
                                        }

                                    }
                                }
                            }
                            alreadyConsumedEventIDs.add(event.getEvent_id());
                        }
                    }
                });
            } else {
                System.err.println("error logging in");
            }
        });
        System.out.println("Tests");
    }
}
