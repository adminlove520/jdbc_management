package com.huang.controller;

import com.huang.entity.LessonTime;
import com.huang.entity.Lesson;
import com.huang.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lessons")
public class LessonController {
    @Autowired
    private LessonService lessonService;

    @Modifying
    @RequestMapping("/getAllLessons")
    public Object getAllLessons() {
        return lessonService.getAllLessons();
    }

    @Modifying
    @RequestMapping("/addCreditTrigger")
    public String addCreditTrigger(@RequestParam(value = "credit") int credit) {
        lessonService.updateCreditWhenMoreThan(credit);  // 调用触发器
        return "success";
    }

    @Modifying
    @RequestMapping("/updateLessonsTime")
    public String updateLessonsTime(@RequestBody LessonTime lessonTime) {
        lessonService.updateLessonsTime(lessonTime);  // 调用存储过程
        return "success";
    }

    @Modifying
    @RequestMapping("/insertLesson")
    public String insertLesson(@RequestBody Lesson lesson) {

        lessonService.insertLesson(lesson);
        return "success";
    }

    @Modifying
    @RequestMapping("/updateLesson")
    public String updateLesson(@RequestBody Lesson lesson) {

        Lesson oldLesson = lessonService.findLessonByLessonNum(lesson.getLessonNum());
        if (oldLesson != null) {
            oldLesson.setLessonName(lesson.getLessonName());
            oldLesson.setYear(lesson.getYear());
            oldLesson.setTerm(lesson.getTerm());
            oldLesson.setCredit(lesson.getCredit());
            oldLesson.setTeacher(lesson.getTeacher());
            lessonService.updateLesson(oldLesson);
        }
        return "success";
    }

    @Modifying
    @RequestMapping("/deleteLesson")
    public String deleteLesson(@RequestParam(value = "lessonNum") String lessonNum) {
        lessonService.deleteLesson(lessonNum);
        return "success";
    }
}
