import Dao.Sql2oDepartmentsDao;
import Dao.Sql2oNewsDao;
import Dao.Sql2oUsersDao;
import com.google.gson.Gson;
import models.Users;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

public class App {
    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
    }

    public static void main(String[] args) {

        port(getHerokuAssignedPort());
        Sql2oNewsDao sql2oNewsDao;
        Sql2oUsersDao sql2oUsersDao;
        Sql2oDepartmentsDao sql2oDepartmentsDao;
        Connection conn;
        Gson gson = new Gson();
        staticFileLocation("/public");

//        String connectionString = "jdbc:postgresql://localhost:5432/organisational_news_portal"; //connect to newsportal, not newsportal_test!
//        Sql2o sql2o = new Sql2o(connectionString, "kingdice", "8203");  //Ubuntu Sql2o sql2o = new Sql2o(connectionString, "user", "1234");


        String connectionString = "postgres://sizpujfvfugluy:6ce0f130ef283176c6f93e3377741f526ab766aef7b96c5d1001bc7ce19513b0@ec2-52-23-45-36.compute-1.amazonaws.com:5432/de5pbv9iaqcr90"; //!
        Sql2o sql2o = new Sql2o(connectionString, "sizpujfvfugluy", "6ce0f130ef283176c6f93e3377741f526ab766aef7b96c5d1001bc7ce19513b0"); //!
        sql2oDepartmentsDao = new Sql2oDepartmentsDao(sql2o);
        sql2oNewsDao = new Sql2oNewsDao(sql2o);
        sql2oUsersDao = new Sql2oUsersDao(sql2o);
        conn = sql2o.open();

        //adding a new user
        post("/users/new", "application/json", (request, response) -> {//tested..............
            Users user = gson.fromJson(request.body(), Users.class);
            sql2oUsersDao.add(user);
            response.status(201);
            return gson.toJson(user);
        });
    }

}
