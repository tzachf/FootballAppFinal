package API;

import DataAccess.Exceptions.NoConnectionException;
import Domain.ErrorLog;
import Domain.FootballManagmentSystem;
import Domain.SeasonManagment.Game;
import Domain.SeasonManagment.IAsset;
import Domain.SeasonManagment.Team;
import Domain.Users.*;
import FootballExceptions.*;
import SpringControllers.RefereeController;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

@RequestMapping("footballapp/referee")
@RestController
public class RefereeRestController {

    private final RefereeController refereeController;




    @Autowired
    public RefereeRestController() {
        refereeController = new RefereeController();
    }

    @CrossOrigin
    @PostMapping("/addEvent")
    public void addEventToGame(@RequestBody Map<String, String> body, final HttpServletResponse response) throws IOException, PersonalPageYetToBeCreatedException {
        boolean flag = false;
        String refereeUserName = body.get("username");
        String eventType = body.get("eventType");
        double minute = Double.parseDouble(body.get("minute"));
        int gameID = Integer.parseInt(body.get("gameID"));
        String playerUserName = body.get("playerusername");
        String alert = "";
        try {
            flag = refereeController.addEventToGame(refereeUserName, eventType, minute, gameID, playerUserName);
        } catch (UserIsNotThisKindOfMemberException e) {
            e.printStackTrace();
            alert = e.getMessage();
        } catch (SQLException e) {
            e.printStackTrace();
            alert = e.getMessage();
        } catch (UserInformationException e) {
            e.printStackTrace();
            alert = e.getMessage();
        } catch (NoConnectionException e) {
            e.printStackTrace();
            alert = e.getMessage();
        } catch (NoPermissionException e) {
            e.printStackTrace();
            alert = e.getMessage();
        } catch (EventNotMatchedException e) {
            e.printStackTrace();
            alert = e.getMessage();
        }
        if (flag) { //todo pop up success
            response.setStatus(HttpServletResponse.SC_ACCEPTED, "Adding event to dame successfully!");

        } else {
            response.sendError(HttpServletResponse.SC_CONFLICT, alert);
            ErrorLog.getInstance().UpdateLog("The error is: " + alert);
        }
    }


    @GetMapping("/players/{gameid}")
    public HashMap<String, String> testGame(@PathVariable String gameid) {
        HashMap<String, String> ans = null;
        try {
            ans = refereeController.gamePlayers(gameid);
        } catch (UserIsNotThisKindOfMemberException e) {
            e.printStackTrace();
        } catch (NoPermissionException e) {
            e.printStackTrace();
        } catch (UserInformationException e) {
            e.printStackTrace();
        } catch (NoConnectionException e) {
            e.printStackTrace();
        }

        return ans;
    }


    /**
     * input: teamID
     * @return key: username
     *         value: teamname + realname
     */
    @GetMapping("/players/{teamid}")
    public HashMap<String, String> testPlayers(@PathVariable String teamid) {

//        Member teamowner = new TeamOwner("Moshe","DASD",123,"asd");
//        Team team = new Team("Bet",((TeamOwner)teamowner));
//        team.getId();
//        Player player = new Player("Jamie", "Lanister", 666, "Sarsei", 222, "bla", new Date());
//        Player player2 = new Player("Apolo", "The King", 234, "Sarsei", 555, "bla", new Date());
//        HashMap<Integer, IAsset> teamList = new HashMap<>();
//        teamList.put(666, player);
//        teamList.put(234, player2);
//        team.setTeamPlayers(teamList);


        UUID teamUUID = UUID.fromString(teamid);

        Team team = (Team)FootballManagmentSystem.getInstance().getTeamByID(teamUUID);
        String teamName = team.getName();
        HashMap<Integer, IAsset> playerList = team.getTeamPlayers();

        HashMap<String, String> ans = new HashMap<>();

        for (Map.Entry curr: playerList.entrySet()){
            String playerUsertName = ((Player)curr.getValue()).getName();
            String playerRealName = ((Player)curr.getValue()).getReal_Name();
            String value = teamName + " - " + playerRealName;
            ans.put(playerUsertName, value);
        }

        return ans;
    }





    @CrossOrigin
    @PostMapping("/addreport")
    public boolean addReportToGame(@RequestBody Map<String, String> body, final HttpServletResponse response) throws PersonalPageYetToBeCreatedException, IOException {
        boolean flag = false;
        String refereeUserName = body.get("username");
        int gameID = Integer.parseInt(body.get("gameID"));
        String alert = "";
        try {
            flag = refereeController.addReportForGame(refereeUserName, gameID);
        } catch (UserIsNotThisKindOfMemberException e) {
            e.printStackTrace();
            alert = e.getMessage();
        } catch (NoPermissionException e) {
            e.printStackTrace();
            alert = e.getMessage();
        } catch (UserInformationException e) {
            e.printStackTrace();
            alert = e.getMessage();
        } catch (NoConnectionException e) {
            e.printStackTrace();
            alert = e.getMessage();
        }
        if (flag) { //todo
            response.setStatus(HttpServletResponse.SC_ACCEPTED, "Your report Added Successfully ! ");
        } else {
            response.sendError(HttpServletResponse.SC_CONFLICT, "Incorrect Login Details");
            ErrorLog.getInstance().UpdateLog("The error is: " + alert);
        }
        return flag;
    }



    @CrossOrigin
    @PostMapping("/setViaMail")
    public void setViaMail(@RequestBody Map<String,String> body, final HttpServletResponse response) throws IOException {
        boolean succeeded=false;
        String alert="";
        String Username = body.get("username");
        String mail = body.get("mail");
        try {
            refereeController.setViaMail(Username,mail);
            succeeded = true;
        } catch (UserIsNotThisKindOfMemberException e) {
            e.printStackTrace();
            alert = e.getMessage();
        } catch (NoPermissionException e) {
            e.printStackTrace();
            alert = e.getMessage();
        } catch (UserInformationException e) {
            e.printStackTrace();
            alert = e.getMessage();
        } catch (SQLException e) {
            e.printStackTrace();
            alert = e.getMessage();
        } catch (EmptyPersonalPageException e) {
            e.printStackTrace();
            alert = e.getMessage();
        } catch (NoConnectionException e) {
            e.printStackTrace();
            alert = e.getMessage();
        }
        if (succeeded) {
            /**pop up success*/
            response.setStatus(HttpServletResponse.SC_ACCEPTED, "RECORDED");
        } else {
            /**pop up failed*/
            response.sendError(HttpServletResponse.SC_CONFLICT, alert);
            ErrorLog.getInstance().UpdateLog("The error is: " + alert);
        }
    }





}




/*
    @GetMapping("/games")
    public String test() {
        games.add(game);
        games.add(game);
        String Message = "[";
        for (Game game:games) {
            int i = 0;
            JSONObject jsonTemp = new JSONObject();
            // jsonTemp.put(game.getObjectId(), game.getHome());
            jsonTemp.put("game-id", game.getObjectId());
            jsonTemp.put("away", game.getAway());
            jsonTemp.put("home", game.getHome());
//            jsonTemp.put("score-home", game.getScoreHome());
//            jsonTemp.put("score-away", game.getScoreAway());
//            jsonTemp.put("date", game.getDateGame());
            Message+=jsonTemp.toString(2);
            i++;
            if (i<games.size()){
                Message+=",";
            }
        }
        Message+="]";
        return Message;
    }
*/





/*
    @PostMapping("/editEvent")
    public boolean editEventToGame(@RequestBody Map<String,String> body) throws PersonalPageYetToBeCreatedException {
        String refereeUserName = body.get("username");
        String eventType = body.get("eventType");
        double minute = Double.parseDouble(body.get("minute"));
        int gameID = Integer.parseInt(body.get("gameID"));
        String playerUserName = body.get("playerusername");
        //Player player = new PlayersDAL().select()
        refereeController.editEventsAfterGame(refereeUserName, , gameID, playerUserName);

        try {
            Game game = new GamesDAL().select(gameID);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (UserInformationException e) {
            e.printStackTrace();
        } catch (UserIsNotThisKindOfMemberException e) {
            e.printStackTrace();
        } catch (NoConnectionException e) {
            e.printStackTrace();
        } catch (NoPermissionException e) {
            e.printStackTrace();
        }
        return true;
    }
    */

