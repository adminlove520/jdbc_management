package com.huang.controller;

import com.huang.entity.Teacher;
import com.huang.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/teachers")
public class TeacherController {
    @Autowired
    private TeacherService teacherService;

    @Modifying
    @RequestMapping("/getAllTeachers")
    public Object getAllTeachers() {
        return teacherService.getAllTeachers();
    }

    @Modifying
    @RequestMapping("/insertTeacher")
    public Object insertTeacher(@RequestBody Teacher teacher) {
        teacherService.insertTeacher(teacher);
        return "success";
    }

    @Modifying
    @RequestMapping("/updateTeacher")
    public Object updateTeacher(@RequestBody Teacher teacher)
    {
        Teacher oldTeacher = teacherService.findTeacherByTeacherNum(teacher.getTeacherNum());
        if (oldTeacher != null)
        {
            oldTeacher.setTeacherName(teacher.getTeacherName());
            oldTeacher.setSex(teacher.getSex());
            oldTeacher.setTitle(teacher.getTitle());
            teacherService.updateTeacher(oldTeacher);
        }
        return "success";
    }

    @Modifying
    @RequestMapping("/deleteTeacher")
    public String deleteTeacher(@RequestParam(value = "teacherNum") String teacherNum)
    {
        teacherService.deleteTeacher(teacherNum);
        return "success";
    }
}
