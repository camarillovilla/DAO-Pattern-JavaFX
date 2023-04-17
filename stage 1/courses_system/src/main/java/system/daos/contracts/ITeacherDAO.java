/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package system.daos.contracts;

import system.daos.implementations.DAOException;
import system.objects.TeacherDTO;

/**
 *
 * @author josue
 */
public interface ITeacherDAO {

    public TeacherDTO authenticateTeacher(String email, String password) throws DAOException;

    public int addTeacher(TeacherDTO teacher) throws DAOException;

    public int updateTeacher(int idTeacher, TeacherDTO teacher) throws DAOException;

}
