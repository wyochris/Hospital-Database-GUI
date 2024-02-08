package main;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Base64;
import java.util.Random;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.swing.JOptionPane;

public class UserLogin {

	private static final Random RANDOM = new SecureRandom();
	private static final Base64.Encoder enc = Base64.getEncoder();
	private static final Base64.Decoder dec = Base64.getDecoder();
	ConnectionService con = null;
	
	public UserLogin(ConnectionService connect) {
		this.con = connect;
	}
	
	public int loginPro(String username, String password) {
		CallableStatement pstmt = null;
		ResultSet rs = null;

	    try {
//	        String query = "SELECT passwordSalt, passwordHash FROM [User] WHERE username = ?";
	        String call = "{? = call [userLoginPro] (?)}";
//	    	pstmt = con.getConnection().prepareStatement(query);
	        pstmt = con.getConnection().prepareCall(call);
	        pstmt.setString(2, username);

	        System.out.println(username);
	        
	        pstmt.registerOutParameter(1, Types.INTEGER);


	        pstmt.execute();
//	        System.out.println(work);
//	        if(!work) {
//		        JOptionPane.showMessageDialog(null, "Login Error");
//		        return 0;
//	        }
	        System.out.println("here");
	        rs = pstmt.getResultSet();
	        if (rs.next()) {
	            byte[] storedSalt = rs.getBytes("PasswordSalt");
	            String storedHash = rs.getString("passwordHash");
	            int returnID = rs.getInt("ID");

	            
	            String hashedPassword = hashPassword(storedSalt, password);
	            
	            if (storedHash.equals(hashedPassword)) {
	            	System.out.println("HELLO"+returnID);
	                return returnID; 
	            }
	        }
	        JOptionPane.showMessageDialog(null, "Login Failed. !");
	        return 0; // Login failed
	    } catch (SQLException e) {
	    	System.out.println("hihihi");
	        JOptionPane.showMessageDialog(null, "Login Failed.");
	        e.printStackTrace();
	        return 0;
	    } finally {
	        try {
	            if (rs != null) rs.close();
	            if (pstmt != null) pstmt.close();
	        } catch (SQLException e) {
	        	System.out.println("hallooo!");
	            e.printStackTrace();
		        JOptionPane.showMessageDialog(null, "Connection Failed.");
		        return 0;
	        }
	    }
	}
	
	public int loginPat(String username, String password) {
		CallableStatement pstmt = null;
		ResultSet rs = null;

	    try {
//	        String query = "SELECT passwordSalt, passwordHash FROM [User] WHERE username = ?";
	        String call = "{? = call [userLoginPat] (?)}";
//	    	pstmt = con.getConnection().prepareStatement(query);
	        pstmt = con.getConnection().prepareCall(call);
	        pstmt.setString(2, username);

	        System.out.println(username);
	        
	        pstmt.registerOutParameter(1, Types.INTEGER);


	        boolean work = pstmt.execute();
	        if(!work) {
		        JOptionPane.showMessageDialog(null, "Login Error");
		        return 0;
	        }
	        rs = pstmt.getResultSet();
	        if (rs.next()) {
	            byte[] storedSalt = rs.getBytes("PasswordSalt");
	            String storedHash = rs.getString("passwordHash");
	            int returnID = rs.getInt("ID");
	            System.out.println("UserLogin" + " " + returnID);
	            
	            String hashedPassword = hashPassword(storedSalt, password);
	            
	            if (storedHash.equals(hashedPassword)) {
	                return returnID; 
	            }
	        }
	        JOptionPane.showMessageDialog(null, "Login Failed. !");
	        return 0; // Login failed
	    } catch (SQLException e) {
	    	System.out.println("hihihi");
	        JOptionPane.showMessageDialog(null, "Login Failed.");
	        e.printStackTrace();
	        return 0;
	    } finally {
	        try {
	            if (rs != null) rs.close();
	            if (pstmt != null) pstmt.close();
	        } catch (SQLException e) {
	        	System.out.println("hallloo");
	            e.printStackTrace();
		        JOptionPane.showMessageDialog(null, "Connection Failed.");
		        return 0;
	        }
	    }
	}
	/*
	 * Register will call register stored procedure if the user exists in provider or patient table
	 * @return if the registration is successful 
	 */
	public boolean register(String firstName, String lastName, String dob, String username, String password, String isProvider, int idNum) {
		System.out.println(idNum);
		String existsProc = "{? = call isUserExists(?, ?, ?, ?, ?)}";
		CallableStatement estmt = null;
		try {
			estmt = con.getConnection().prepareCall(existsProc);
			estmt.setString(2, firstName);
			estmt.setString(3, lastName);
			estmt.setString(5, isProvider);
			estmt.setInt(6, idNum);

			
            Date date = Date.valueOf(dob);
			estmt.setDate(4, date);
			estmt.registerOutParameter(1, Types.INTEGER);
			estmt.executeUpdate();
			
            int returnCode = estmt.getInt(1);
            if (returnCode != 0) {
				JOptionPane.showMessageDialog(null, "Registration failed. Error Code: " + returnCode);
            	return false;
            }
		}
		catch(SQLException e) {
			JOptionPane.showMessageDialog(null, "Registration failed.");
			System.out.println(e);
			return false;
		}
		
		String storedProc = "{? = call Register(?, ?, ?, ?)}";
		CallableStatement cstmt = null;
		try {
			byte[] salt = getNewSalt();
			cstmt = con.getConnection().prepareCall(storedProc);
			cstmt.setString(2, username);
			cstmt.setBytes(3, salt);
			cstmt.setString(4, hashPassword(salt, password));
			cstmt.setInt(5, idNum);
			
			cstmt.registerOutParameter(1, Types.INTEGER);

			cstmt.execute();
			
			int returnCode = cstmt.getInt(1);
			if(returnCode != 0) {
				JOptionPane.showMessageDialog(null, "Registration failed.");
				System.out.println("returnCode: " + returnCode);
				return false;
			}
			else {
				return true;
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if (cstmt != null) cstmt.close();
			}
			catch (SQLException e) {
		        JOptionPane.showMessageDialog(null, "Connection Failed.");

				e.printStackTrace(); 
			} 
		}
		return false;
	}
	
	public byte[] getNewSalt() {
		byte[] salt = new byte[16];
		RANDOM.nextBytes(salt);
		return salt;
	}
	
	public String getStringFromBytes(byte[] data) {
		return enc.encodeToString(data);
	}

	public String hashPassword(byte[] salt, String password) {
		
		KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
		SecretKeyFactory f;
		byte[] hash = null;
		try {
			f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			hash = f.generateSecret(spec).getEncoded();
		} catch (NoSuchAlgorithmException e) {
			JOptionPane.showMessageDialog(null, "An error occurred");
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			JOptionPane.showMessageDialog(null, "An error occurred");
			e.printStackTrace();
		}
		return getStringFromBytes(hash);
	}
	
}
