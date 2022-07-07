package model.entities.dao.impl;

import db.DB;
import db.DbException;
import model.entities.Department;
import model.entities.Seller;
import model.entities.dao.SellerDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellerDaoJDBC implements SellerDao {

    private Connection connection;

    public SellerDaoJDBC(Connection connection){
        this.connection = connection;
    }

    @Override
    public void insert(Seller seller) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(
                    "INSERT INTO seller " +
                            "(Name, Email, BirthDate, BaseSalary, DepartmentId) " +
                            "VALUES " +
                            "(?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, seller.getName());
            preparedStatement.setString(2, seller.getEmail());
            preparedStatement.setDate(3, new java.sql.Date(seller.getBirthDate().getTime()));
            preparedStatement.setDouble(4, seller.getBaseSalary());
            preparedStatement.setInt(5, seller.getDepartment().getId());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0){
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                if(resultSet.next()){
                    int id = resultSet.getInt(1);
                    seller.setId(id);
                }
                DB.closeResultSet(resultSet);
            } else {
                throw new DbException("Unexpected error! No rows affected!");
            }
        } catch (SQLException e){
            throw new DbException(e.getMessage());
        }finally {
            DB.closeStatement(preparedStatement);
        }
    }

    @Override
    public void update(Seller seller) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(
                    "UPDATE seller " +
                            "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? " +
                            "WHERE Id = ?",
                    Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, seller.getName());
            preparedStatement.setString(2, seller.getEmail());
            preparedStatement.setDate(3, new java.sql.Date(seller.getBirthDate().getTime()));
            preparedStatement.setDouble(4, seller.getBaseSalary());
            preparedStatement.setInt(5, seller.getDepartment().getId());
            preparedStatement.setInt(6, seller.getId());

            preparedStatement.executeUpdate();


        } catch (SQLException e){
            throw new DbException(e.getMessage());
        }finally {
            DB.closeStatement(preparedStatement);
        }
    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(
                    "DELETE FROM seller " +
                            "WHERE Id = ?");

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            throw new DbException(e.getMessage());
        }finally {
            DB.closeStatement(preparedStatement);
        }
    }

    @Override
    public Seller findById(Integer id) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            statement = connection.prepareStatement(
                    "SELECT seller.*,department.Name as DepName " +
                            "FROM seller INNER JOIN department " +
                            "ON seller.DepartmentId = department.Id " +
                            "WHERE seller.Id = ?"
            );

            statement.setInt(1, id);
            resultSet = statement.executeQuery();

            if(resultSet.next()){
                Department department = instantiateDepartment(resultSet);

                return instantiateSeller(resultSet, department);
            }

            return null;
        }catch (SQLException e){
            throw new DbException(e.getMessage());
        }finally {
            DB.closeStatement(statement);
            DB.closeResultSet(resultSet);
        }

    }

    private Department instantiateDepartment(ResultSet resultSet) throws SQLException{

        Department department = new Department();
        department.setId(resultSet.getInt("DepartmentId"));
        department.setName(resultSet.getString("DepName"));
        return department;

    }

    private Seller instantiateSeller(ResultSet resultSet, Department department) throws SQLException{

        Seller seller = new Seller();
        seller.setId(resultSet.getInt("Id"));
        seller.setName(resultSet.getString("Name"));
        seller.setEmail(resultSet.getString("Email"));
        seller.setBaseSalary(resultSet.getDouble("BaseSalary"));
        seller.setBirthDate(resultSet.getDate("BirthDate"));
        seller.setDepartment(department);
        return seller;

    }

    @Override
    public List<Seller> findAll() {
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            statement = connection.prepareStatement(
                    "SELECT seller.*,department.Name as DepName " +
                            "FROM seller INNER JOIN department " +
                            "ON seller.DepartmentId = department.Id " +
                            "ORDER BY Name"
            );

            resultSet = statement.executeQuery();

            List<Seller> listSellers = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>();

            while (resultSet.next()){

                Department dep = map.get(resultSet.getInt("DepartmentId"));

                if (dep == null){
                    dep = instantiateDepartment(resultSet);
                    map.put(resultSet.getInt("DepartmentId"), dep);
                }

                Seller seller = instantiateSeller(resultSet, dep);

                listSellers.add(seller);

            }

            return listSellers;

        }catch (SQLException e){
            throw new DbException(e.getMessage());
        }finally {
            DB.closeStatement(statement);
            DB.closeResultSet(resultSet);
        }
    }

    @Override
    public List<Seller> findByDepartmentId(Department department) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            statement = connection.prepareStatement(
                    "SELECT seller.*,department.Name as DepName " +
                            "FROM seller INNER JOIN department " +
                            "ON seller.DepartmentId = department.Id " +
                            "WHERE DepartmentId = ? " +
                            "ORDER BY Name"
            );

            statement.setInt(1, department.getId());
            resultSet = statement.executeQuery();

            List<Seller> listSellers = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>();

            while (resultSet.next()){

                Department dep = map.get(resultSet.getInt("DepartmentId"));

                if (dep == null){
                    dep = instantiateDepartment(resultSet);
                    map.put(resultSet.getInt("DepartmentId"), dep);
                }

                Seller seller = instantiateSeller(resultSet, dep);

                listSellers.add(seller);

            }

            return listSellers;

        }catch (SQLException e){
            throw new DbException(e.getMessage());
        }finally {
            DB.closeStatement(statement);
            DB.closeResultSet(resultSet);
        }

    }
}
