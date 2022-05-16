package Dao;

import models.Department_News;
import models.Departments;
import models.News;
import models.Users;

import java.util.List;

public interface NewsDao {
    //create
    //create
    void addNews(News news);
    void addDepartmentNews(Department_News department_news);

    //read

    List<News> getAll();
    News findById(int id);

    //update
    //delete

    void clearAll();
}
