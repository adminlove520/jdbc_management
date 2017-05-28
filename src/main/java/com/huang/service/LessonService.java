package com.huang.service;

import com.huang.entity.Lesson;
import com.huang.entity.LessonTime;
import com.huang.entity.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
public class LessonService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Lesson> getAllLessons() {
        final String sql = "SELECT lesson.*, teacher.teacherName, teacher.sex, teacher.title FROM lesson, teacher WHERE lesson.teacherNum = teacher.teacherNum";  // 混合查询

        List<Lesson> lessonList = jdbcTemplate.query(sql, new LessonRowMapper());

        return lessonList;
    }

    public Lesson findLessonByLessonNum(String lessonNum) {
        final String sql = "SELECT lesson.*, teacher.teacherName, teacher.sex, teacher.title FROM lesson, teacher WHERE lesson.teacherNum = teacher.teacherNum AND lesson.lessonNum = ?";  // 混合查询

        return jdbcTemplate.queryForObject(sql, new Object[]{lessonNum}, new LessonRowMapper());
    }

    public void insertLesson(Lesson lesson) {
        final String sql = "INSERT INTO lesson(lessonNum, lessonName, year, term, credit, teacherNum) VALUES (?,?,?,?,?,?)";

        jdbcTemplate.update(sql, new Object[]{lesson.getLessonNum(), lesson.getLessonName(),
                lesson.getYear(), lesson.getTerm(), lesson.getCredit(),
                lesson.getTeacher().getTeacherNum()});
    }

    public void deleteLesson(String lessonNum) {
        final String sql = "DELETE FROM lesson WHERE lessonNum = ?";

        jdbcTemplate.update(sql, lessonNum);
    }

    public void updateLesson(Lesson lesson) {
        final String sql = "UPDATE lesson SET lessonName = ?, year = ?, term = ?, credit = ?, teacherNum = ? WHERE lessonNum = ?";

        jdbcTemplate.update(sql, new Object[]{lesson.getLessonName(),
                lesson.getYear(), lesson.getTerm(), lesson.getCredit(),
                lesson.getTeacher().getTeacherNum(), lesson.getLessonNum()});
    }

    // 触发器相关方法
    public void updateCreditWhenMoreThan(int credit){
        final String dropTriggerSql = "DROP TRIGGER IF EXISTS update_credit_when_more_than;";
        final String triggerSql = "CREATE TRIGGER update_credit_when_more_than AFTER INSERT ON lesson FOR EACH ROW " +
                "BEGIN IF new.credit > ? THEN UPDATE student SET credit = ? WHERE lessonNum = new.lessonNum; END IF; END";

        jdbcTemplate.execute(dropTriggerSql);
        jdbcTemplate.update(triggerSql, new Object[]{credit, credit});
    }

    // 创建存储过程相关方法
//    public void createProcedure(String year, String term){
//        final String procedureSql = "CREATE OR REPLACE PROCEDURE update_lessons_time_proc(IN param1 VARCHAR(16), IN param2 VARCHAR(8)) " +
//                "BEGIN UPDATE lesson SET year = param1, term = param2; " +
//                "END";
//
//        jdbcTemplate.update(procedureSql);
//    }

    // 调用存储过程相关方法
    public void updateLessonsTime(LessonTime lessonTime){
        final String callSql = "CALL update_lessons_time_proc(?,?)";

        jdbcTemplate.update(callSql, new Object[]{lessonTime.getYear(), lessonTime.getTerm()});
    }

}

class LessonRowMapper implements RowMapper<Lesson> {
    @Override
    public Lesson mapRow(ResultSet resultSet, int i) throws SQLException {
        Lesson lesson = new Lesson();
        lesson.setLessonNum(resultSet.getString("lessonNum"));
        lesson.setLessonName(resultSet.getString("lessonName"));
        lesson.setYear(resultSet.getString("year"));
        lesson.setTerm(resultSet.getString("term"));
        lesson.setCredit(resultSet.getInt("credit"));

        Teacher teacher = new Teacher(resultSet.getString("teacherNum"),
                resultSet.getString("teacherName"), resultSet.getString("sex"),
                resultSet.getString("title"));

        lesson.setTeacher(teacher);

        return lesson;

    }
}
