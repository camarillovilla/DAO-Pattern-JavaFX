/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package system.daos.contracts;

import javafx.collections.ObservableList;
import system.daos.implementations.DAOException;
import system.objects.CourseDTO;

/**
 *
 * @author josue
 */
public interface ICourseDAO {

    public ObservableList<CourseDTO> getCoursesForTeacher(int idTeacher) throws DAOException;

    public int addCourse(CourseDTO course) throws DAOException;

    public int updateCourse(int idCourse, CourseDTO course) throws DAOException;

    public int deleteCourse(int idCourse) throws DAOException;

}
