package main;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.CallableStatement;
import java.sql.Date;
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
	
	public int loginPro(String username, char[] pass) {
		CallableStatement pstmt = null;
		ResultSet rs = null;

	    try {
	        String call = "{? = call [userLoginPro] (?)}";
	        pstmt = con.getConnection().prepareCall(call);
	        pstmt.setString(2, username);

	        System.out.println(username);
	        
	        pstmt.registerOutParameter(1, Types.INTEGER);


	        pstmt.execute();
	        
	        rs = pstmt.getResultSet();
	        if (rs.next()) {
	            byte[] storedSalt = rs.getBytes("PasswordSalt");
	            String storedHash = rs.getString("passwordHash");
	            int returnID = rs.getInt("ID");

	            
	            String hashedPassword = hashPassword(storedSalt, pass);
	            
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
	            e.printStackTrace();
		        JOptionPane.showMessageDialog(null, "Connection Failed.");
		        return 0;
	        }
	    }
	}

	public int loginPat(String username, char[] password) {
		CallableStatement pstmt = null;
		ResultSet rs = null;
		int patID = 0;
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
		        return patID;
	        }
	        rs = pstmt.getResultSet();
	        if (rs.next()) {
	            byte[] storedSalt = rs.getBytes("PasswordSalt");
	            String storedHash = rs.getString("passwordHash");
	            patID = rs.getInt("ID");
	            
	            String hashedPassword = hashPassword(storedSalt, password);
	            
	            if (storedHash.equals(hashedPassword)) {
	                return patID; 
	            }
	        }
//	        JOptionPane.showMessageDialog(null, "Login Failed. !!");
	        return patID; // Login failed
	    } catch (SQLException e) {
	        JOptionPane.showMessageDialog(null, "Login Failed.");
	        e.printStackTrace();
	        return patID;
	    } finally {
	        try {
	            if (rs != null) rs.close();
	            if (pstmt != null) pstmt.close();
	        } catch (SQLException e) {
	        	System.out.println("hallloo");
	            e.printStackTrace();
		        JOptionPane.showMessageDialog(null, "Connection Failed.");
		        return patID;
	        }
	    }
	}
	/*
	 * Register will call register stored procedure if the user exists in provider or patient table
	 * @return if the registration is successful 
	 */
	public int register(String firstName, String lastName, Date dob, String username, char[] password, String isProvider, int idNum) {
		String existsProc = "{? = call isUserExists(?, ?, ?, ?, ?)}";
		CallableStatement estmt = null;
		try {
			estmt = con.getConnection().prepareCall(existsProc);
			estmt.setString(2, firstName);
			estmt.setString(3, lastName);
			estmt.setString(5, isProvider);
			estmt.setInt(6, idNum);

			
			estmt.setDate(4, dob);
			estmt.registerOutParameter(1, Types.INTEGER);
			estmt.executeUpdate();
			
            int returnCode = estmt.getInt(1);
            
            if (returnCode != 10) {
            	return returnCode;
            	
            }
		}
		catch(SQLException e) {
			JOptionPane.showMessageDialog(null, "Registration failed" + e);
			e.printStackTrace();
			return 100;
		}
		
		System.out.println("made it past part 1");
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
			
			return returnCode;

            
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
		return 100;
	}
	
	public byte[] getNewSalt() {
		byte[] salt = new byte[16];
		RANDOM.nextBytes(salt);
		return salt;
	}
	
	public String getStringFromBytes(byte[] data) {
		return enc.encodeToString(data);
	}

	public String hashPassword(byte[] salt, char[] password) {
		
		KeySpec spec = new PBEKeySpec(password, salt, 65536, 128);
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
	
	
	public int getHospitalID(String name) {
		int hosID = 0;
		CallableStatement pstmt = null;
		ResultSet rs = null;

	    try {
	        String call = "{? = call [isHospital] (?)}";
	        pstmt = con.getConnection().prepareCall(call);
	        pstmt.setString(2, name);

	        System.out.println(name);
	        
	        pstmt.registerOutParameter(1, Types.INTEGER);


	        pstmt.execute();

	        rs = pstmt.getResultSet();
	        if (rs.next()) {
	            hosID = rs.getInt("ID");
	            return hosID; 
	        }
	        JOptionPane.showMessageDialog(null, "Hospital Failed. !!!!!");
	        return 0; // Login failed
	    } catch (SQLException e) {
	        JOptionPane.showMessageDialog(null, "Hospital Failed.");
	        e.printStackTrace();
	        return 0;
	    } finally {
	        try {
	            if (rs != null) rs.close();
	            if (pstmt != null) pstmt.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
		        JOptionPane.showMessageDialog(null, "Connection Failed.");
		        return 0;
	        }
	    }
		
	}
	
}
