//package main;
//
//import javax.swing.*;
//import javax.swing.table.DefaultTableModel;
//import java.sql.ResultSet;
//import java.sql.ResultSetMetaData;
//import java.sql.SQLException;
//import java.sql.Statement;
//
//public class updateMedicineCode {
//    <buttonName>.addActionListener(e -> {
//
//            try {
//                String storedProcedureCall = "{? = call updateMedicine(?, ?, ?)}";
//                field1text = field1.getText();
//                field2text = field2.getText();
//                field3text = field3.getText();
//                field3int = Integer.parseInt(field3text);
//
//
//
//                try {
//                    CallableStatement cs = connection.prepareCall(storedProcedureCall);
//                    cs.setString(2, field1text);
//                    cs.setString(3, field2text);
//                    cs.setInteger(4, field3int);
//
//
//                    cs.registerOutParameter(1, java.sql.Types.INTEGER);
//                    cs.executeUpdate();
//                    int returnCode = cs.getInt(1);
//                    if (returnCode == 0) {
//                        JOptionPane.showMessageDialog(null, "Medicine Updated!");
//                    } else {
//                        if (returnCode == 1) {
//                            JOptionPane.showMessageDialog(null, "Error: Medicine, Patient ID, and Dose cannot be null");
//                        } else if(returnCode == 2){
//                            JOptionPane.showMessageDialog(null, "Error: Patient ID cannot be negative.");
//                        } else if(returnCode == 3) {
//                            JOptionPane.showMessageDialog(null, "Error: Patient does not exist.");
//                        } else if(returnCode == 4) {
//                            JOptionPane.showMessageDialog(null, "Error: Medicine does not exist.");
//                        }
//                        else if(returnCode==5){
//                            JOptionPane.showMessageDialog(null, "Error: Patient is not taking this medication.");
//                        }
//
//                        else {
//                            JOptionPane.showMessageDialog(null, "Error: Unknown error occurred.");
//                        }
//
//                    }
//                } catch (SQLException er) {
//                    throw new RuntimeException(er);
//                }
//
//                Statement stmt = connection.createStatement();
//                ResultSet rs = stmt.executeQuery("SELECT * FROM <make new view for doctors to view patients and symptoms and medicine of those patients>");
//
////                frame.add(textPanel, BorderLayout.SOUTH);
///
//                DefaultTableModel tableModel = new DefaultTableModel();
//                ResultSetMetaData rsmd = rs.getMetaData();
//                int columnsNumber = rsmd.getColumnCount();
//
//                // Add column headers
//                for (int i = 1; i <= columnsNumber; i++) {
//                    tableModel.addColumn(rsmd.getColumnName(i));
//                }
//
//                // Add data rows
//                while (rs.next()) {
//                    Object[] rowData = new Object[columnsNumber];
//                    for (int i = 1; i <= columnsNumber; i++) {
//                        rowData[i - 1] = rs.getString(i);
//                    }
//                    tableModel.addRow(rowData);
//                }
//
//                // Create JTable with the table model
//                resultTable = new JTable(tableModel);
//
//
//
//                // Add the table to the result panel
//                resultPanel.removeAll();
//                resultPanel.add(new JScrollPane(resultTable));
//
//                // Add the result panel to the frame
//                frame.add(resultPanel, BorderLayout.CENTER);
//                frame.revalidate();
//
//
//            } catch (SQLException ex) {
//                throw new RuntimeException(ex);
//            }
////            doctorView.setVisible(true);
////            patientView.setVisible(true);
////            success.setVisible(true);
////            addPatientButton.setVisible(true);
////            confirmAddPatientButton.setVisible(false);
////            resultPanel.setVisible(true);
////            field1.setVisible(false);
////            field2.setVisible(false);
////            field3.setVisible(false);
////            field4.setVisible(false);
////            cancelButton.setVisible(true);
//
//
//        });
//}
