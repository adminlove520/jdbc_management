package com.huang.service;

import com.huang.entity.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
public class TeacherService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Teacher> getAllTeachers() {
        final String sql = "SELECT * FROM teacher";

        List<Teacher> teacherList = jdbcTemplate.query(sql, new TeacherRowMapper());

        return teacherList;
    }

    public Teacher findTeacherByTeacherNum(String teacherNum) {
        final String sql = "SELECT * FROM teacher WHERE teacherNum = ?";

        return jdbcTemplate.queryForObject(sql, new Object[]{teacherNum}, new TeacherRowMapper());
    }

    public void insertTeacher(Teacher teacher) {
        final String sql = "INSERT INTO teacher(teacherNum, teacherName, sex, title) VALUES (?,?,?,?)";

        jdbcTemplate.update(sql, new Object[]{teacher.getTeacherNum(), teacher.getTeacherName(),
                teacher.getSex(), teacher.getTitle()});
    }

    public void deleteTeacher(String teacherNum) {
        final String sql = "DELETE FROM teacher WHERE teacherNum = ?";

        jdbcTemplate.update(sql, teacherNum);
    }

    public void updateTeacher(Teacher teacher) {
        final String sql = "UPDATE teacher SET teacherName = ?, sex = ?, title = ? WHERE teacherNum = ?";

        jdbcTemplate.update(sql, new Object[]{teacher.getTeacherName(),
                teacher.getSex(), teacher.getTitle(), teacher.getTeacherNum()});
    }

}

class TeacherRowMapper implements RowMapper<Teacher> {
    @Override
    public Teacher mapRow(ResultSet resultSet, int i) throws SQLException {
        Teacher teacher = new Teacher();
        teacher.setTeacherNum(resultSet.getString("teacherNum"));
        teacher.setTeacherName(resultSet.getString("teacherName"));
        teacher.setSex(resultSet.getString("sex"));
        teacher.setTitle(resultSet.getString("title"));
        return teacher;

    }
}
