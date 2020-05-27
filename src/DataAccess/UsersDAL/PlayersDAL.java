package DataAccess.UsersDAL;

import DataAccess.AlertsDAL.MemberAlertsDAL;
import DataAccess.DAL;
import DataAccess.Exceptions.DuplicatedPrimaryKeyException;
import DataAccess.Exceptions.NoConnectionException;
import DataAccess.Exceptions.mightBeSQLInjectionException;
import DataAccess.SeasonManagmentDAL.AssetsDAL;
import DataAccess.SeasonManagmentDAL.TeamsDAL;
import DataAccess.UserInformationDAL.PersonalPagesDAL;
import Domain.Alerts.IAlert;
import Domain.SeasonManagment.IAsset;
import Domain.SeasonManagment.Team;
import Domain.Users.Member;
import Domain.Users.PersonalInfo;
import Domain.Users.Player;
import FootballExceptions.NoPermissionException;
import FootballExceptions.UserInformationException;
import FootballExceptions.UserIsNotThisKindOfMemberException;
import javafx.util.Pair;

import java.sql.*;
import java.util.LinkedList;
import java.util.Queue;

public class PlayersDAL implements DAL<Member, String> {

    Connection connection = null;

    @Override
    public boolean insert(Member member) throws SQLException, NoConnectionException, UserInformationException, mightBeSQLInjectionException, NoPermissionException, UserIsNotThisKindOfMemberException, DuplicatedPrimaryKeyException {

//        new MembersDAL().insert(member);
//        member = ((Player) member);
//        new AssetsDAL().insert((IAsset) member);
//
//        connection = this.connect();
//
//        String statement = "INSERT INTO players (UserName,DateOfBirth,Team,PersonalPage,Role, AssetID,FootballRate) VALUES (?,?,?,?,?,?,?);";
//        PreparedStatement preparedStatement = connection.prepareStatement(statement);
//        preparedStatement.setString(1, member.getName());
//
//        java.util.Date date = ((Player) member).getDateOfBirth();
//        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
//        preparedStatement.setDate(2, sqlDate);
//
//        if (((Player) member).getMyTeam() == null) {
//            preparedStatement.setString(3, "0");
//        } else {
//            preparedStatement.setString(3, ((Player) member).getMyTeam().getId().toString());
//        }
//        if (((Player) member).getInfo() != null) {
//            preparedStatement.setInt(4, ((Player) member).getInfo().getPageID());
//        } else {
//            preparedStatement.setInt(4, 0);
//        }
//        preparedStatement.setString(5, ((Player) member).getRole());
//        preparedStatement.setInt(6, ((Player) member).getAssetID());
//        preparedStatement.setDouble(7, ((Player) member).getFootballRate());
//        preparedStatement.execute();
//        connection.close();
        return true;
    }

    @Override
    public boolean update(Member member) throws SQLException, UserIsNotThisKindOfMemberException, UserInformationException, NoConnectionException, NoPermissionException, mightBeSQLInjectionException, DuplicatedPrimaryKeyException {

        /**MEMBER DETAILS UPDATE*/
        new MembersDAL().update(member);

        if (checkExist(member.getName(), "fans", "UserName","")) {

        }
        connection = connect();

        String statement = "UPDATE players SET DateOfBirth=?,Team=?,PersonalPage=?,Role=?,AssetID=?,FootballRate=? WHERE UserName=?";
        PreparedStatement preparedStatement = connection.prepareStatement(statement);
        java.util.Date date = ((Player) member).getDateOfBirth();
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        preparedStatement.setDate(1, sqlDate);

        if (((Player) member).getMyTeam() == null) {
            preparedStatement.setString(2, "0");
        } else {
            preparedStatement.setString(2, ((Player) member).getMyTeam().getId().toString());
        }
        if (((Player) member).getInfo() != null) {
            preparedStatement.setInt(3, ((Player) member).getInfo().getPageID());
        } else {
            preparedStatement.setInt(3, 0);
        }
        preparedStatement.setString(4, ((Player) member).getRole());
        preparedStatement.setInt(5, ((Player) member).getAssetID());
        preparedStatement.setDouble(6, ((Player) member).getFootballRate());
        preparedStatement.setString(7,member.getName());
        int ans = preparedStatement.executeUpdate();
        new AssetsDAL().update(((IAsset) member));
        connection.close();
        return ans==1;
    }

    @Override
    public Member select(String userName) throws SQLException, UserInformationException, UserIsNotThisKindOfMemberException, NoConnectionException, NoPermissionException {

        connection = connect();

        /**MEMBER DETAILS*/
        String statement = "SELECT Password,RealName,MailAddress,isActive, AlertsViaMail FROM members WHERE UserName = ?;";
        PreparedStatement preparedStatement = connection.prepareStatement(statement);
        preparedStatement.setString(1, userName);
        ResultSet rs = preparedStatement.executeQuery();

        if (!rs.next()) {
            throw new UserInformationException();
        }
        String password = rs.getString(1);
        String realName = rs.getString(2);
        String mail = rs.getString(3);
        boolean isActive = rs.getBoolean(4);
        boolean AlertsViaMail = rs.getBoolean(5);

        statement = "SELECT alertObjectID FROM member_alerts WHERE memberUserName = ? ;";
        preparedStatement = connection.prepareStatement(statement);
        preparedStatement.setString(1,userName);
        rs = preparedStatement.executeQuery();
        Queue <IAlert> memberAlerts = new LinkedList<>();
        while (!rs.next()){
            memberAlerts.add(new MemberAlertsDAL().select(new Pair<String, String>(userName,rs.getString(1))).getKey().getValue());
        }



        /**PLAYER DETAILS*/
        statement = "SELECT Team,PersonalPage,Role, AssetID,FootballRate,DateOfBirth FROM players WHERE UserName = ?;";
        preparedStatement = connection.prepareStatement(statement);
        preparedStatement.setString(1, userName);
        rs = preparedStatement.executeQuery();

        if (!rs.next()) {
            throw new UserIsNotThisKindOfMemberException();
        }

        String teamID = rs.getString(1);
        Team team = new TeamsDAL().select(teamID);
        int personlPID = rs.getInt(2);
        PersonalInfo page = new PersonalPagesDAL().select(personlPID);
        String role = rs.getString(3);
        int assetID = rs.getInt(4);
        double rate = rs.getDouble(5);
        java.util.Date dateOfBirth = rs.getDate(6);

        /**ASSET DETAILS*/
        statement = "SELECT Value FROM assets WHERE AssetID = ?;";
        preparedStatement = connection.prepareStatement(statement);
        preparedStatement.setInt(1, assetID);
        rs = preparedStatement.executeQuery();
        rs.next();
        int assetVal = rs.getInt(1);

        Member member = new Player(userName,password,realName,memberAlerts,isActive,AlertsViaMail,mail,assetVal,assetID,team,role,page,dateOfBirth,rate);
        connection.close();
        return member;
    }

    @Override
    public boolean delete(String userName) {
        return false;
    }
}
