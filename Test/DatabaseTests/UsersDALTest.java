package DatabaseTests;

import DataAccess.AlertsDAL.MemberAlertsDAL;
import DataAccess.Exceptions.DuplicatedPrimaryKeyException;
import DataAccess.Exceptions.NoConnectionException;
import DataAccess.Exceptions.mightBeSQLInjectionException;
import DataAccess.UserInformationDAL.PersonalPagesDAL;
import DataAccess.UsersDAL.CommissionersDAL;
import DataAccess.UsersDAL.FansDAL;
import DataAccess.UsersDAL.PlayersDAL;
import DataAccess.UsersDAL.TeamManagerDAL;
import Domain.FootballManagmentSystem;
import Domain.Users.*;
import FootballExceptions.*;
import javafx.util.Pair;
import org.junit.Test;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UsersDALTest {


    @Test
    public void login() throws mightBeSQLInjectionException, DuplicatedPrimaryKeyException, NoPermissionException, SQLException, UserInformationException, UserIsNotThisKindOfMemberException, NoConnectionException {
        FootballManagmentSystem.getInstance().login("aviluzon","12345678");
    }
    @Test
    public void alertsTest() throws SQLException, mightBeSQLInjectionException, NoConnectionException {
            boolean ans =new MemberAlertsDAL().checkExist(new Pair<>("ref4","79199029-e703-4772-b728-09813e630197"),"member_alerts","MemberUserName","AlertObjectID");
        System.out.println(ans);
    }
    @Test
    public void fanSelect() throws UserIsNotThisKindOfMemberException, SQLException, UserInformationException, NoConnectionException, NoPermissionException, AlreadyFollowThisPageException, mightBeSQLInjectionException, DuplicatedPrimaryKeyException, EmptyPersonalPageException {
        Fan fan = new FansDAL().select("Ozi9",true);
        PersonalInfo personalInfo = new PersonalPagesDAL().select(31528149,true);
        fan.turnAlertForPersonalPageOn(personalInfo);
        assertEquals("Ozi Gofia",fan.getReal_Name());
    }

    @Test
    public void test() throws NoPermissionException, EmptyPersonalPageException, SQLException, UserInformationException, UserIsNotThisKindOfMemberException, NoConnectionException {
        PersonalInfo personalInfo = new PersonalPagesDAL().select(28745299,true);
        System.out.println("FAS");
    }

    @Test
    public void test2() throws mightBeSQLInjectionException, DuplicatedPrimaryKeyException, NoPermissionException, SQLException, UserInformationException, UserIsNotThisKindOfMemberException, NoConnectionException {
        TeamOwner teamOwner = new TeamOwner("ASD","SAD",0,"asd");
    }
    @Test
    public void teamOwnerWithoutTeam() throws mightBeSQLInjectionException, DuplicatedPrimaryKeyException, NoPermissionException, SQLException, UserInformationException, UserIsNotThisKindOfMemberException, NoConnectionException {
        TeamOwner teamOwner = new TeamOwner("Hogeg","Moshe Hogeg",0,"Hogeg");
    }

    @Test
    public void fanInsert() throws UserIsNotThisKindOfMemberException, SQLException, UserInformationException, NoConnectionException, NoPermissionException, AlreadyFollowThisPageException, mightBeSQLInjectionException, DuplicatedPrimaryKeyException, EmptyPersonalPageException {
        Fan fan = new Fan("Ozi9","Ozi Gofia",0,"asd");
        List<PersonalInfo> list = new LinkedList<>();
        PersonalInfo personalInfo = new PersonalPagesDAL().select(31528149,true);
        list.add(personalInfo);
        fan.addPersonalPagesToFollow(list);

    }

    @Test
    public void playerSelect() throws UserIsNotThisKindOfMemberException, SQLException, UserInformationException, NoConnectionException, NoPermissionException {
        Player player = new PlayersDAL().select("CR723",true);
        assertTrue(player.getName().equals("CR723"));
        assertTrue(player.getMyTeam().getName().equals("Beiter"));
    }

    @Test
    public void teamManagerSelect() throws UserIsNotThisKindOfMemberException, SQLException, UserInformationException, NoConnectionException, NoPermissionException {
        TeamManager teamManager = new TeamManagerDAL().select("CR723",true);
        assertEquals("Ronaldo",teamManager.getReal_Name());
    }

    @Test
    public void commissionerSelect() throws UserIsNotThisKindOfMemberException, SQLException, UserInformationException, NoConnectionException, NoPermissionException {
        Commissioner commissioner = new CommissionersDAL().select("Alon",true);
        assertEquals("Tzahi",commissioner.getReal_Name());
    }

    @Test
    public void teamOwnerSelect(){

    }




}
