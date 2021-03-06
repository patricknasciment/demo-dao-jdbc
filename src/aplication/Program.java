package aplication;

import model.entities.Department;
import model.entities.Seller;
import model.entities.dao.DaoFactory;
import model.entities.dao.SellerDao;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Program {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        SellerDao sellerDao = DaoFactory.createSellerDao();

        Seller seller = sellerDao.findById(3);
        System.out.println("-------- Test 1: seller findById ---------");
        System.out.println(seller);
        System.out.println("\n-------- Test 2: seller findByDepartment ---------");
        Department department = new Department(2, null);
        List<Seller> sellers = sellerDao.findByDepartmentId(department);
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

        System.out.println("\n-------- Test 4: seller insert---------");
        Seller newSeller = new Seller(null, "Josias", "josias@gmail.com", new Date(), 3000.0, department);
        sellerDao.insert(newSeller);
        System.out.println("Inserted! new id = " + newSeller.getId());

        System.out.println("\n-------- Test 5: seller insert---------");
        seller = sellerDao.findById(1);
        seller.setName("Carlinhos Pereira");
        sellerDao.update(seller);
        System.out.println("Updated completed");

        System.out.println("\n-------- Test 5: seller delete---------");
        System.out.println("Enter id for delete test: ");
        int id = sc.nextInt();
        sellerDao.deleteById(id);
        sc.close();
    }
}
