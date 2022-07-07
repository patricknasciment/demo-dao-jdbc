package aplication;

import model.entities.Department;
import model.entities.Seller;
import model.entities.dao.DaoFactory;
import model.entities.dao.SellerDao;

import java.util.List;

public class Program {

    public static void main(String[] args) {
        SellerDao sellerDao = DaoFactory.createSellerDao();

        Seller seller = sellerDao.findById(3);
        System.out.println("-------- Test 1: seller findById ---------");
        System.out.println(seller);
        System.out.println("\n-------- Test 2: seller findByDepartment ---------");
        List<Seller> sellers = sellerDao.findByDepartmentId(new Department(2, null));
        for (Seller obj:
             sellers) {
            System.out.println(obj);
        }

        System.out.println("\n-------- Test 3: seller findByAll---------");
        sellers = sellerDao.findAll();
        for (Seller obj:
                sellers) {
            System.out.println(obj);
        }
    }
}
