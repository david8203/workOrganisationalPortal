package Dao;

import models.DepartmentNews;
import models.News;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

public class Sql2oNewsDao {
    private final Sql2o sql2o;

    public Sql2oNewsDao(Sql2o sql2o) {

        this.sql2o = sql2o;
    }
    @Override
    public void addNews(News news) {
        try(Connection con=sql2o.open()) {
            String sql="INSERT INTO news (news_type,department_id,user_id,title,description) VALUES (:news_type," +
                    ":department_id,:user_id,:title,:description)";
            int id= (int) con.createQuery(sql,true)
                    .bind(news)
                    .executeUpdate()
                    .getKey();
            news.setId(id);
        }catch (Sql2oException e){
            System.out.println(e);
        }

    }
    @Override
    public void addDepartmentNews(DepartmentNews department_news) {
        try(Connection con=sql2o.open()) {
            String sql="INSERT INTO news (news_type,department_id,user_id,title,description) VALUES (:news_type," +
                    ":department_id,:user_id,:title,:description)";
            int id= (int) con.createQuery(sql,true)
                    .bind(department_news)
                    .executeUpdate()
                    .getKey();
            department_news.setId(id);

        }catch (Sql2oException e){
            System.out.println(e);
        }

    }

}
