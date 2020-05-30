package API;


import Domain.ErrorLog;
import Domain.FootballManagmentSystem;
import Domain.SeasonManagment.Leaugue;
import Domain.Users.TeamOwner;
import SpringControllers.CommissionerController;
import SpringControllers.TeamOwnerController;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RequestMapping("footballapp/teamowner")
@RestController
public class TeamOwnerRestController {


    private final TeamOwnerController ownerController;


    @Autowired
    public TeamOwnerRestController() {
        this.ownerController = new TeamOwnerController();
    }


    @CrossOrigin
    @PostMapping("/signteam")
    public void signUpTeam(@RequestBody Map<String,String> body, final HttpServletResponse response) throws IOException {
        boolean succeeded;
        String ownerUsername = body.get("username");
        int leagueId = Integer.parseInt(body.get("leagueID"));
        int year = Integer.parseInt(body.get("year"));
        String teamName = (body.get("team_name"));
        succeeded = ownerController.sendRegisterRequestForNewTeam(ownerUsername,teamName,leagueId,year);
        if (succeeded){
            /**pop up success*/
            response.setStatus(HttpServletResponse.SC_ACCEPTED, "Sign Team Request Added Successfully ! ");
        }else {
            /**pop up failed*/
            response.sendError(HttpServletResponse.SC_CONFLICT,"Incorrect Details");
            ErrorLog.getInstance().UpdateLog("The error is: " + "Incorrect Details");

        }
    }


    @CrossOrigin
    @GetMapping("/leagues")
    public List<Integer> getLeagues(){
        FootballManagmentSystem system = FootballManagmentSystem.getInstance();
        List<Leaugue> leaugues = system.getAllLeagus();
        List<Integer> leauguesID = new LinkedList<>();
        Leaugue leaugue = new Leaugue();
        leaugue.setId(2);
        Leaugue leaugue1 = new Leaugue();
        leaugue1.setId(22);
        leauguesID.add(leaugue.getID());
        leauguesID.add(leaugue1.getID());
        leaugues.add(leaugue1);

        String message = "[";
        int i = 0;
        for (Leaugue league:leaugues) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("leagueID", league.getID());
            message+= jsonObject.toString(2);
            i++;
            if (i<leaugues.size()){
                message+=",";
            }
        }
        message += "]";

        return leauguesID;
    }



}
