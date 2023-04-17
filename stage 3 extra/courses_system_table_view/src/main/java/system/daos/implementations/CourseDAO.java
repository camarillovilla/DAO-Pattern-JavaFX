/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package system.daos.implementations;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import system.controllers.App;
import system.daos.contracts.ICourseDAO;
import system.objects.CourseDTO;
import system.objects.Status;

/**
 *
 * @author josue
 */
public class CourseDAO implements ICourseDAO {

    private final String GET_COURSES_LIST_QUERY = "SELECT * FROM courses where courses.idTeacher = ?";
    private final String ADD_COURSE_COMMAND = "INSERT INTO courses (`nrc`, `name`, `courseType`, `idTeacher`) VALUES (?, ?, ?, ?)";
    private final String UPDATE_COURSE_COMMAND = "UPDATE courses SET `nrc` = ?, `name` = ?, `courseType` = ?, `idTeacher` = ? WHERE (`id` = ?)";
    private final String DELETE_COURSE_COMMAND = "DELETE FROM courses WHERE (`id` = ?)";
    private final String GET_COURSE_BY_NRC_QUERY = "SELECT * FROM courses WHERE courses.nrc = ?";
    private final String GET_COURSE_BY_ID_QUERY = "SELECT * FROM courses WHERE courses.id = ?";

    @Override
    public ObservableList<CourseDTO> getCoursesForTeacher(int idTeacher) throws DAOException {
        ObservableList<CourseDTO> courses = FXCollections.observableArrayList();
        try {
            checkIdTeacherValue(idTeacher);
            courses = executeGetCoursesForTeacherQuery(idTeacher);
        } catch (IllegalArgumentException ex) {
            throw new DAOException(ex.getMessage(), Status.WARNING);
        }
        return courses;
    }

    @Override
    public int addCourse(CourseDTO course) throws DAOException {
        try {
            CourseDTO validatedCourse = getValidatedCourseForAdd(course);
            return executeCourseAdditionTransaction(validatedCourse);
        } catch (IllegalArgumentException ex) {
            throw new DAOException(ex.getMessage(), Status.WARNING);
        }
    }

    @Override
    public int updateCourse(int idCourse, CourseDTO course) throws DAOException {
        try {
            CourseDTO validatedCourse = getValidatedCourseForUpdate(idCourse, course);
            return executeCourseUpdateTransaction(idCourse, validatedCourse);
        } catch (IllegalArgumentException ex) {
            throw new DAOException(ex.getMessage(), Status.WARNING);
        }
    }

    @Override
    public int deleteCourse(int idCourse) throws DAOException {
        try {
            checkIdTeacherValue(idCourse);
            return executeCourseDeleteTransaction(idCourse);
        } catch (IllegalArgumentException ex) {
            throw new DAOException(ex.getMessage(), Status.WARNING);
        }
    }

    private ObservableList<CourseDTO> executeGetCoursesForTeacherQuery(int idTeacher) throws DAOException {
        ObservableList<CourseDTO> courses = FXCollections.observableArrayList();
        try {
            PreparedStatement statement = DBConnection.getInstance().prepareStatement(GET_COURSES_LIST_QUERY);
            statement.setInt(1, idTeacher);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                courses.add(new CourseDTO(
                        rs.getInt("id"),
                        rs.getString("nrc"),
                        rs.getString("name"),
                        rs.getString("courseType"),
                        rs.getTimestamp("createdAt").toLocalDateTime(),
                        rs.getTimestamp("updatedAt").toLocalDateTime(),
                        rs.getInt("idTeacher")));
            }
        } catch (SQLException ex) {
            App.getLogger().fatal(ex.getMessage());
            throw new DAOException("No fue posible obtener la información de los cursos", Status.ERROR);
        } finally {
            DBConnection.close();
        }
        return courses;
    }

    private int executeCourseAdditionTransaction(CourseDTO course) throws DAOException {
        int response = -1;
        try {
            DBConnection.getInstance().setAutoCommit(false);
            PreparedStatement statement = DBConnection.getInstance().prepareStatement(
                    ADD_COURSE_COMMAND,
                    Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, course.getNrc());
            statement.setString(2, course.getName());
            statement.setString(3, course.getCourseType());
            statement.setInt(4, course.getIdTeacher());
            statement.executeUpdate();
            DBConnection.getInstance().commit();
            ResultSet rs = statement.getGeneratedKeys();
            while (rs.next()) {
                response = rs.getInt(1);
            }
        } catch (SQLException ex) {
            App.getLogger().fatal(ex.getMessage());
            DBConnection.rollback();
            throw new DAOException("No fue posible registrar el curso", Status.ERROR);
        } finally {
            DBConnection.close();
        }
        return response;
    }

    private int executeCourseUpdateTransaction(int idCourse, CourseDTO course) throws DAOException {
        int response = -1;
        try {
            DBConnection.getInstance().setAutoCommit(false);
            PreparedStatement statement = DBConnection.getInstance().prepareStatement(UPDATE_COURSE_COMMAND);
            statement.setString(1, course.getNrc());
            statement.setString(2, course.getName());
            statement.setString(3, course.getCourseType());
            statement.setInt(4, course.getIdTeacher());
            statement.setInt(5, idCourse);
            statement.executeUpdate();
            DBConnection.getInstance().commit();
            response = idCourse;
        } catch (SQLException ex) {
            App.getLogger().fatal(ex.getMessage());
            DBConnection.rollback();
            throw new DAOException("No fue posible actualizar el curso", Status.ERROR);
        } finally {
            DBConnection.close();
        }
        return response;
    }

    private int executeCourseDeleteTransaction(int idCourse) throws DAOException {
        int response = -1;
        try {
            DBConnection.getInstance().setAutoCommit(false);
            PreparedStatement statement = DBConnection.getInstance().prepareStatement(
                    DELETE_COURSE_COMMAND);
            statement.setInt(1, idCourse);
            statement.executeUpdate();
            DBConnection.getInstance().commit();
            response = idCourse;
        } catch (SQLException ex) {
            App.getLogger().fatal(ex.getMessage());
            DBConnection.rollback();
            throw new DAOException("No fue posible eliminar el curso", Status.ERROR);
        } finally {
            DBConnection.close();
        }
        return response;
    }

    private void checkIdTeacherValue(int idTeacher) throws DAOException {
        if (idTeacher <= 0) {
            throw new IllegalArgumentException("El identificador del académico no es válido");
        }
    }

    private CourseDTO getValidatedCourseForAdd(CourseDTO course) throws DAOException {
        checkNrcDuplication(course.getNrc());
        return course;
    }

    private CourseDTO getValidatedCourseForUpdate(int idCourse, CourseDTO course) throws DAOException {
        CourseDTO oldCourse = getCourseById(idCourse);
        if (oldCourse.getId() <= 0) {
            throw new DAOException("No se encontró ningún curso con ese ID", Status.ERROR);
        } else {
            if (!oldCourse.getNrc().equalsIgnoreCase(course.getNrc())) {
                checkNrcDuplication(course.getNrc());
            }
        }
        return course;
    }

    private CourseDTO getCourseById(int idCourse) throws DAOException {
        CourseDTO course = new CourseDTO();
        course.setId(-1);
        try {
            PreparedStatement statement = DBConnection.getInstance().prepareStatement(GET_COURSE_BY_ID_QUERY);
            statement.setInt(1, idCourse);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                course.setId(rs.getInt("id"));
                course.setNrc(rs.getString("nrc"));
                course.setName(rs.getString("name"));
                course.setCourseType(rs.getString("courseType"));
                course.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
                course.setUpdatedAt(rs.getTimestamp("updatedAt").toLocalDateTime());
                course.setIdTeacher(rs.getInt("idTeacher"));
            }
        } catch (SQLException ex) {
            App.getLogger().fatal(ex.getMessage());
            DBConnection.close();
            throw new DAOException("No fue posible consultar la información de los cursos", Status.ERROR);
        }
        return course;
    }

    private void checkNrcDuplication(String nrc) throws DAOException {
        try {
            PreparedStatement statement = DBConnection.getInstance().prepareStatement(GET_COURSE_BY_NRC_QUERY);
            statement.setString(1, nrc);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                throw new IllegalArgumentException("Ya existe un curso registrado con el mismo NRC");
            }
        } catch (SQLException ex) {
            App.getLogger().fatal(ex.getMessage());
            DBConnection.close();
            throw new DAOException("No fue posible consultar la información de los cursos", Status.ERROR);
        }
    }

}
