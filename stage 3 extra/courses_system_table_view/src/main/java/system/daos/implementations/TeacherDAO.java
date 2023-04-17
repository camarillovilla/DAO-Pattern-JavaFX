/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package system.daos.implementations;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import system.controllers.App;
import system.daos.contracts.ITeacherDAO;
import system.objects.Status;
import system.objects.TeacherDTO;

/**
 *
 * @author josue
 */
public class TeacherDAO implements ITeacherDAO {

    private final String GET_TEACHER_QUERY = "SELECT * FROM teachers WHERE teachers.email = ? AND teachers.password = ?";
    private final String ADD_TEACHER_COMMAND = "INSERT INTO teachers (`email`, `password`, `name`, `studyGrade`) VALUES (?, ?, ?, ?)";
    private final String UPDATE_TEACHER_COMMAND = "UPDATE teachers SET `email` = ?, `password` = ?, `name` = ?, `studyGrade` = ? WHERE (`id` = ?)";
    private final String DELETE_TEACHER_COMMAND = "DELETE FROM teachers WHERE teachers.id = ?";
    private final String GET_TEACHER_BY_EMAIL_QUERY = "SELECT * FROM teachers WHERE teachers.email = ?";
    private final String GET_TEACHER_BY_ID_QUERY = "SELECT * FROM teachers WHERE teachers.id = ?";

    @Override
    public TeacherDTO authenticateTeacher(String email, String password) throws DAOException {
        try {
            TeacherDTO validatedTeacher = createValidatedTeacherForLogin(email, password);
            validatedTeacher = executeTeacherQuery(validatedTeacher);
            return validatedTeacher;
        } catch (IllegalArgumentException ex) {
            throw new DAOException(ex.getMessage(), Status.WARNING);
        }
    }

    @Override
    public int addTeacher(TeacherDTO teacher) throws DAOException {
        try {
            TeacherDTO validatedTeacher = getValidatedTeacherForAdd(teacher);
            return executeTeacherAdditionTransaction(validatedTeacher);
        } catch (IllegalArgumentException ex) {
            throw new DAOException(ex.getMessage(), Status.WARNING);
        }
    }

    @Override
    public int updateTeacher(int idTeacher, TeacherDTO teacher) throws DAOException {
        try {
            TeacherDTO validatedTeacher = getValidatedTeacherForUpdate(teacher);
            return executeTeacherUpdateTransaction(idTeacher, validatedTeacher);
        } catch (IllegalArgumentException ex) {
            throw new DAOException(ex.getMessage(), Status.WARNING);
        }
    }

    private TeacherDTO executeTeacherQuery(TeacherDTO validatedTeacher) throws DAOException {
        try {
            PreparedStatement statement = DBConnection.getInstance().prepareStatement(GET_TEACHER_QUERY);
            statement.setString(1, validatedTeacher.getEmail());
            statement.setString(2, validatedTeacher.getPassword());
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                validatedTeacher.setId(rs.getInt("id"));
                validatedTeacher.setEmail(rs.getString("email"));
                validatedTeacher.setPassword(rs.getString("password"));
                validatedTeacher.setName(rs.getString("name"));
                validatedTeacher.setStudyGrade(rs.getString("studyGrade"));
                validatedTeacher.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
                validatedTeacher.setUpdatedAt(rs.getTimestamp("updatedAt").toLocalDateTime());
            }
        } catch (SQLException ex) {
            App.getLogger().fatal(ex.getMessage());
            throw new DAOException("No fue posible obtener la información del académico", Status.ERROR);
        } finally {
            DBConnection.close();
        }
        return validatedTeacher;
    }

    private int executeTeacherAdditionTransaction(TeacherDTO teacher) throws DAOException {
        int response = -1;
        try {
            DBConnection.getInstance().setAutoCommit(false);
            PreparedStatement statement = DBConnection.getInstance().prepareStatement(
                    ADD_TEACHER_COMMAND,
                    Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, teacher.getEmail());
            statement.setString(2, teacher.getPassword());
            statement.setString(3, teacher.getName());
            statement.setString(4, teacher.getStudyGrade());
            statement.executeUpdate();
            DBConnection.getInstance().commit();
            ResultSet rs = statement.getGeneratedKeys();
            while (rs.next()) {
                response = rs.getInt(1);
            }
        } catch (SQLException ex) {
            App.getLogger().fatal(ex.getMessage());
            DBConnection.rollback();
            throw new DAOException("No fue posible registrar al académico", Status.ERROR);
        } finally {
            DBConnection.close();
        }
        return response;
    }

    private int executeTeacherUpdateTransaction(int idTeacher, TeacherDTO teacher) throws DAOException {
        int response = -1;
        try {
            DBConnection.getInstance().setAutoCommit(false);
            PreparedStatement statement = DBConnection.getInstance().prepareStatement(UPDATE_TEACHER_COMMAND);
            statement.setString(1, teacher.getEmail());
            statement.setString(2, teacher.getPassword());
            statement.setString(3, teacher.getName());
            statement.setString(4, teacher.getStudyGrade());
            statement.setInt(5, idTeacher);
            statement.executeUpdate();
            DBConnection.getInstance().commit();
            response = idTeacher;
        } catch (SQLException ex) {
            App.getLogger().fatal(ex.getMessage());
            DBConnection.rollback();
            throw new DAOException("No fue posible actualizar un académico", Status.ERROR);
        } finally {
            DBConnection.close();
        }
        return response;
    }

    private TeacherDTO createValidatedTeacherForLogin(String email, String password) {
        TeacherDTO teacher = TeacherDTO.cleanSession();
        teacher.setEmail(email);
        teacher.setPassword(password);
        return teacher;
    }

    private TeacherDTO getValidatedTeacherForAdd(TeacherDTO teacher) throws DAOException {
        checkEmailDuplication(teacher.getEmail());
        return teacher;
    }

    private TeacherDTO getValidatedTeacherForUpdate(TeacherDTO teacher) throws DAOException {
        TeacherDTO oldTeacher = getTeacherById(teacher.getId());
        if (oldTeacher.getId() <= 0) {
            throw new DAOException("No se encontró ningún académico con ese Id", Status.FATAL);
        } else {
            if (!oldTeacher.getEmail().equalsIgnoreCase(teacher.getEmail())) {
                checkEmailDuplication(teacher.getEmail());
            }
        }
        return teacher;

    }

    private TeacherDTO getTeacherById(int id) throws DAOException {
        TeacherDTO teacher = TeacherDTO.getNewInstance();
        teacher.setId(-1);
        try {
            PreparedStatement statement = DBConnection.getInstance().prepareStatement(GET_TEACHER_BY_ID_QUERY);
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                teacher.setId(rs.getInt("id"));
                teacher.setEmail(rs.getString("email"));
                teacher.setPassword(rs.getString("password"));
                teacher.setName(rs.getString("name"));
                teacher.setStudyGrade(rs.getString("studyGrade"));
                teacher.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
                teacher.setUpdatedAt(rs.getTimestamp("updatedAt").toLocalDateTime());
            }
        } catch (SQLException ex) {
            App.getLogger().fatal(ex.getMessage());
            DBConnection.close();
            throw new DAOException("No fue posible obtener la información del académico", Status.ERROR);
        }
        return teacher;
    }

    private void checkEmailDuplication(String email) throws DAOException {
        try {
            PreparedStatement statement = DBConnection.getInstance().prepareStatement(GET_TEACHER_BY_EMAIL_QUERY);
            statement.setString(1, email);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                throw new IllegalArgumentException("El email ya se encuentra registrado en el sistema");
            }
        } catch (SQLException ex) {
            DBConnection.close();
            throw new DAOException("No fue posible validar tu información con la base de datos del sistema", Status.ERROR);
        }
    }

    public void deleteTeacher(int idTeacher) throws DAOException {
        try {
            PreparedStatement statement = DBConnection.getInstance().prepareStatement(DELETE_TEACHER_COMMAND);
            statement.setInt(1, idTeacher);
            int rs = statement.executeUpdate();
            if (rs <= 0) {
                throw new DAOException("No fue posible eliminar al académico", Status.ERROR);
            }
        } catch (SQLException ex) {
            App.getLogger().fatal(ex.getMessage());
            throw new DAOException("No fue posible eliminar al académico", Status.ERROR);
        } finally {
            DBConnection.close();
        }
    }

}
