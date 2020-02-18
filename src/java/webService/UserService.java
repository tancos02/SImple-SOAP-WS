/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webService;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
/**
 *
 * @author paulus
 */
@WebService(serviceName = "UserService")
public class UserService {
    Connection conn = null;
    PreparedStatement pstm = null;
    ResultSet rs = null;
    ConnectionMySQL connectSQL = new ConnectionMySQL();
 
    @WebMethod(operationName = "tampilDaftarUser")
    public ArrayList < User> tampilDaftarUser() {
        ArrayList < User > user = new ArrayList < > ();
        try {
            conn = connectSQL.getConnect();
            pstm = conn.prepareStatement("select saldo," +
                "nama, nama_bank, id from user");
            rs = pstm.executeQuery();
            while (rs.next()) {
                User brg = new User();
                brg.setSaldo(rs.getInt("saldo"));
                brg.setNama(rs.getString("nama"));
                brg.setNamaBank(rs.getString("nama_bank"));
                brg.setId(rs.getInt("id"));
                user.add(brg);
            }
        } catch (Exception e) {
            System.out.println("Gagal Tampil :" + e.toString());
        }
        return user;
    }
    
    @WebMethod(operationName = "addVA")
    public int addVA(
        @WebParam(name = "id_asal") int id_asal,
        @WebParam(name = "id_tujuan") int id_tujuan
    ) {
        int va = 99999999;
        try {
            conn = connectSQL.getConnect();
            pstm = conn.prepareStatement("insert into virtual_account(id_va,id_user) values (?,?)");
            va = id_asal + 10000000;
            pstm.setInt(1, va);
            pstm.setInt(2, id_tujuan);
            pstm.executeUpdate();
            return va;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return va;
        }
    }
    
    @WebMethod(operationName = "transferUang")
    public boolean transferUang(
        @WebParam(name = "uang") int uang,
        @WebParam(name = "id_asal") int id_asal,
        @WebParam(name = "id_va") int id_va
    ) {
        boolean isTransferSuccess = false;
        try {
            conn = connectSQL.getConnect();
            pstm = conn.prepareStatement("select saldo from user where id = ?");
            pstm.setInt(1, id_asal);
            rs = pstm.executeQuery();
            int hasil = 0;
            while (rs.next()) {
                hasil = rs.getInt("saldo");
                if (hasil - uang > 0) {
                    hasil = hasil - uang;
                    isTransferSuccess = true;
                }
            }
            if(isTransferSuccess) {
                conn = connectSQL.getConnect();
                pstm = conn.prepareStatement("update user set saldo = ? where id = ?");
                pstm.setInt(1,hasil);
                pstm.setInt(2, id_asal);
                pstm.executeUpdate();
                
                conn = connectSQL.getConnect();
                pstm = conn.prepareStatement("select saldo,id from user join virtual_account where user.id = virtual_account.id_user and id_va = ?");
                pstm.setInt(1, id_va);
                rs = pstm.executeQuery();
                hasil = 0;
                int id = 0;
                while (rs.next()) {
                    id = rs.getInt("id");
                    hasil = rs.getInt("saldo");
                    hasil = hasil + uang;
                }
                
                conn = connectSQL.getConnect();
                pstm = conn.prepareStatement("update user set saldo = ? where id = ?");
                pstm.setInt(1,hasil);
                pstm.setInt(2, id);
                pstm.executeUpdate();
            }
            return isTransferSuccess;
        } catch (Exception e) {
            System.out.println("Failed to edit saldo because " + e.toString());
            return isTransferSuccess;
        }
    }
    
    @WebMethod(operationName = "validateUser")
    public boolean validateUser(
        @WebParam(name = "id") int id
    ) {
        User user = new User();
        try {
            conn = connectSQL.getConnect();
            pstm = conn.prepareStatement("select * from user where id = ?");
            pstm.setInt(1, id);
            rs = pstm.executeQuery();
            while (rs.next()) {
                user.setId(rs.getInt("id"));
                user.setNama(rs.getString("nama"));
                user.setNamaBank(rs.getString("nama_bank"));
                user.setSaldo(rs.getInt("saldo"));
            }
            if (user.getId() > 0) {
                return true;
            }
            return false;
        } catch (Exception e) {
            System.out.println("Failed to get user data because " + e.toString());
            return false;
        }
    }
    
    @WebMethod(operationName = "getUser")
    public User getUser(
        @WebParam(name = "id") int id
    ) {
        User user = new User();
        try {
            conn = connectSQL.getConnect();
            pstm = conn.prepareStatement("select * from user where id = ?");
            pstm.setInt(1, id);
            rs = pstm.executeQuery();
            while (rs.next()) {
                user.setId(rs.getInt("id"));
                user.setNama(rs.getString("nama"));
                user.setNamaBank(rs.getString("nama_bank"));
                user.setSaldo(rs.getInt("saldo"));
            }
            return user;
        } catch (Exception e) {
            System.out.println("Failed to get user data because " + e.toString());
            return user;
        }
    }
    
    @WebMethod(operationName = "getAuthId")
    public int getAuthId() {
        int auth_id = 0;
        try {
            conn = connectSQL.getConnect();
            pstm = conn.prepareStatement("select id from auth_id");
            rs = pstm.executeQuery();
            while (rs.next()) {
                auth_id = rs.getInt("id");
            }
            return auth_id;
        } catch (Exception e) {
            System.out.println("Failed to get user data because " + e.toString());
            return auth_id;
        }
    }
    
    @WebMethod(operationName = "updateAuthId")
    public boolean updateAuthId(
        @WebParam(name = "id") int id
    ) {
        boolean success = false;
        try {
            conn = connectSQL.getConnect();
            pstm = conn.prepareStatement("update auth_id set id = ?");
            pstm.setInt(1,id);
            pstm.executeUpdate();
            success = true;
            return success;
        } catch (Exception e) {
            System.out.println("Failed to get user data because " + e.toString());
            return success;
        }
    }
}
