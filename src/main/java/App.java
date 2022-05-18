import Dao.Sql2oDepartmentsDao;
import Dao.Sql2oNewsDao;
import Dao.Sql2oUsersDao;
import Exceptions.ApiException;
import com.google.gson.Gson;
import models.Departments;
import models.News;
import models.Users;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.List;

import static spark.Spark.*;
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
        post("/departments/new","application/json",(request, response) -> {//tested................
            Departments departments =gson.fromJson(request.body(),Departments.class);
            sql2oDepartmentsDao.add(departments);
            response.status(201);
            return gson.toJson(departments);
        });
        //adding users to a specific department
        post("/add/user/:user_id/department/:department_id","application/json",(request, response) -> {//tested......

            int user_id=Integer.parseInt(request.params("user_id"));
            int department_id=Integer.parseInt(request.params("department_id"));
            Departments departments=sql2oDepartmentsDao.findById(department_id);
            Users users=sql2oUsersDao.findById(user_id);
            if(departments==null){
                throw new ApiException(404, String.format("No department with the id: \"%s\" exists",
                        request.params("department_id")));
            }
            if(users==null){
                throw new ApiException(404, String.format("No user with the id: \"%s\" exists",
                        request.params("user_id")));
            }
            sql2oDepartmentsDao.addUserToDepartment(users,departments);

            List<Users> departmentUsers=sql2oDepartmentsDao.getAllUsersInDepartment(departments.getId());

            response.status(201);
            return gson.toJson(departmentUsers);
        });
        post("/news/new/department","application/json",(request, response) -> { //tested.......
            News department_news =gson.fromJson(request.body(), News.class);
            Departments departments=sql2oDepartmentsDao.findById(department_news.getDepartment_id());
            Users users=sql2oUsersDao.findById(department_news.getUser_id());
            if(departments==null){
                throw new ApiException(404, String.format("No department with the id: \"%s\" exists",
                        request.params("id")));
            }
            if(users==null){
                throw new ApiException(404, String.format("No user with the id: \"%s\" exists",
                        request.params("id")));
            }
            sql2oNewsDao.addNews(department_news);
            response.status(201);
            return gson.toJson(department_news);
        });
        post("/news/new/general","application/json",(request, response) -> {//tested

            News news =gson.fromJson(request.body(),News.class);
            sql2oNewsDao.addNews(news);
            response.status(201);
            return gson.toJson(news);
        });
        //getting users in the department

        get("/users", "application/json", (request, response) -> {//tested

            if(sql2oDepartmentsDao.getAll().size() > 0){
                return gson.toJson(sql2oUsersDao.getAll());
            }
            else {
                return "{\"RESPONSE\":\"NO USERS CURRENTLY\"}";
            }
        });
        //path to show departments
        get("/departments","application/json",(request, response) -> {//tested.............
            if(sql2oDepartmentsDao.getAll().size()>0){
                return gson.toJson(sql2oDepartmentsDao.getAll());
            }
            else {
                return "{\"RESPONSE\":\"NO DEPARTMENTS CURRENTLY\"}";
            }
        });


        //path to get listed general news
        get("/news/general","application/json",(request, response) -> {//tested.....works!!
            if(sql2oNewsDao.getAll().size()>0){
                return gson.toJson(sql2oNewsDao.getAll());
            }
            else {
                return "{\"RESPONSE\":\"NO NEWS AVAILABLE\"}";
            }
        });
        //path to show departments
        get("/departments","application/json",(request, response) -> {//tested.............
            if(sql2oDepartmentsDao.getAll().size()>0){
                return gson.toJson(sql2oDepartmentsDao.getAll());
            }
            else {
                return "{\"RESPONSE\":\"NO DEPARTMENTS CURRENTLY\"}";
            }
        });


        //path to get listed general news
        get("/news/general","application/json",(request, response) -> {//tested.....works!!
            if(sql2oNewsDao.getAll().size()>0){
                return gson.toJson(sql2oNewsDao.getAll());
            }
            else {
                return "{\"RESPONSE\":\"NO NEWS AVAILABLE\"}";
            }
        });
        //path to get each user department
        get("/user/:id/departments","application/json",(request, response) -> {//tested ......works
            int id=Integer.parseInt(request.params("id"));
            if(sql2oUsersDao.getAllUserDepartments(id).size()>0){
                return gson.toJson(sql2oUsersDao.getAllUserDepartments(id));
            }
            else {
                return "{\"RESPONSE\":\"DEPARTMENT NOT AVAILABLE\"}";
            }
        });
        //getting specific user..................
        get("/user/:id", "application/json", (request, response) -> {//tested and works...........
            int id=Integer.parseInt(request.params("id"));
            if(sql2oUsersDao.findById(id)==null){
                throw new ApiException(404, String.format("No user with the id: \"%s\" exists",
                        request.params("id")));
            }
            else {
                return gson.toJson(sql2oUsersDao.findById(id));
            }
        });
        get("/department/:id/users","application/json",(request, response) -> {//tested works............
            int id=Integer.parseInt(request.params("id"));
            if(sql2oDepartmentsDao.getAllUsersInDepartment(id).size()>0){
                return gson.toJson(sql2oDepartmentsDao.getAllUsersInDepartment(id));
            }
            else {
                return "{\"message\":\"I'm sorry, but department has no users.\"}";
            }
        });
        get("/department/:id","application/json",(request, response) -> {//works.......
            int id=Integer.parseInt(request.params("id"));
            if(sql2oDepartmentsDao.findById(id)==null){
                throw new ApiException(404, String.format("No department with the id: %s exists",
                        request.params("id")));
            }
            else {
                return gson.toJson(sql2oDepartmentsDao.findById(id));
            }
        });
    }

}
