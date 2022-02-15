package todolist.dao;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import todolist.model.ToDoList;
import todolist.model.ToDoTask;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

public class ToDoJdbcDao extends JdbcDaoSupport implements ToDoDao {
    public ToDoJdbcDao(DataSource dataSource) {
        super();
        setDataSource(dataSource);
    }

    @Override
    public int addToDoTask(ToDoTask toDoTask) {
        String sql = "INSERT INTO ToDoTask (listId, title, done) VALUES (?, ?, ?)";
        assert getJdbcTemplate() != null;
        return getJdbcTemplate().update(sql, toDoTask.getListId(), toDoTask.getTitle(), toDoTask.isDone());
    }

    @Override
    public void deleteToDoTask(int id) {
        String sql = "DELETE FROM ToDoTask WHERE id = ?";
        assert getJdbcTemplate() != null;
        getJdbcTemplate().update(sql, id);
    }

    @Override
    public void deleteToDoTasksByListId(int listId) {
        String sql = "DELETE FROM ToDoTask WHERE listId = ?";
        assert getJdbcTemplate() != null;
        getJdbcTemplate().update(sql, listId);
    }

    @Override
    public void markToDoTask(int id, boolean done) {
        String sql = "UPDATE ToDoTask SET done = ? WHERE id = ?";
        assert getJdbcTemplate() != null;
        getJdbcTemplate().update(sql, done, id);
    }

    @Override
    public List<ToDoTask> getToDoTaskByListId(int listId) {
        String sql = "SELECT * FROM ToDoTask WHERE listId = ?";
        assert getJdbcTemplate() != null;
        return getJdbcTemplate().query(sql, new BeanPropertyRowMapper(ToDoTask.class), listId);
    }

    @Override
    public int addToDoList(ToDoList toDoList) {
        String sql = "INSERT INTO ToDoList (title) VALUES (?)";
        assert getJdbcTemplate() != null;
        return getJdbcTemplate().update(sql, toDoList.getTitle());
    }

    @Override
    public Optional<ToDoList> getToDoList(int id) {
        String sql = "SELECT * FROM ToDoList WHERE id = ?";
        assert getJdbcTemplate() != null;
        return getJdbcTemplate().query(sql, new BeanPropertyRowMapper(ToDoList.class), id).stream().findFirst();
    }

    @Override
    public void deleteToDoList(int id) {
        String sql = "DELETE FROM ToDoList WHERE id = ?";
        assert getJdbcTemplate() != null;
        getJdbcTemplate().update(sql, id);
    }

    @Override
    public List<ToDoList> getToDoLists() {
        String sql = "SELECT * FROM ToDoList";
        assert getJdbcTemplate() != null;
        return getJdbcTemplate().query(sql, new BeanPropertyRowMapper(ToDoList.class));
    }
}
