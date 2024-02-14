package main;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

public abstract class User {
	
	static final int scrollWidth = 1500;
	static final int scrollHeight = 800;
		
	public abstract void initializeUserScreen();
	
	public void initalizeTable(ResultSet rs, JTable resultTable, JPanel resultPanel, JFrame frame) {
		try {
		DefaultTableModel tableModel = new DefaultTableModel();
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnsNumber = rsmd.getColumnCount();

		// Add column headers
		for (int i = 1; i <= columnsNumber; i++) {
			tableModel.addColumn(rsmd.getColumnName(i));
		}

		// Add data rows
		while (rs.next()) {
			Object[] rowData = new Object[columnsNumber];
			for (int i = 1; i <= columnsNumber; i++) {
				rowData[i - 1] = rs.getString(i);
				System.out.println("STRING:" + rs.getString(i));
			}
			tableModel.addRow(rowData);
			System.out.println("ROWDATA:" + rowData.toString());
		}

		// Create JTable with the table model
		resultTable = new JTable(tableModel);
		

		
//		resultTable.addMouseListener(new MouseAdapter() {
//		    @Override
//		    public void mouseClicked(final MouseEvent e) {
//		        if (e.getClickCount() == 1) {
//		            final JTable jTable= (JTable)e.getSource();
//		            int row = jTable.getSelectedRow();
//		            int column = jTable.getSelectedColumn();
//		            String valueInCell = (String)jTable.getValueAt(row, column);
//		            System.out.println(valueInCell);
//		        }
//		    }
//		});
		
		
//		resultTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		// Add the table to the result panel
		resultPanel.removeAll();
		resultPanel.add(new JScrollPane(resultTable));
		

		
		JScrollPane scroll = new JScrollPane(resultTable);
		scroll.setPreferredSize(new Dimension(scrollWidth, scrollHeight));
		resultPanel.add(scroll);

		
		// Add the result panel to the frame
		frame.add(resultPanel, BorderLayout.CENTER);
		frame.revalidate();
		} catch(SQLException ex){
			throw new RuntimeException(ex);
		}
		
	}
	
	public JTable initalizeTableRETURN(ResultSet rs) {
		try {
		DefaultTableModel tableModel = new DefaultTableModel();
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnsNumber = rsmd.getColumnCount();

		// Add column headers
		for (int i = 1; i <= columnsNumber; i++) {
			tableModel.addColumn(rsmd.getColumnName(i));
		}

		// Add data rows
		while (rs.next()) {
			Object[] rowData = new Object[columnsNumber];
			for (int i = 1; i <= columnsNumber; i++) {
				rowData[i - 1] = rs.getString(i);
			}
			tableModel.addRow(rowData);
		}

		
		// Create JTable with the table model
		return new JTable(tableModel);
		


//		resultTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		// Add the table to the result panel
		//COMMENTING OUT TO PUT IN DIFFERENT FUNCTION
		
//		resultPanel.removeAll();
//		resultPanel.add(new JScrollPane(resultTable));
//		
//
//		
//		JScrollPane scroll = new JScrollPane(resultTable);
//		scroll.setPreferredSize(new Dimension(1000, 1000));
//		resultPanel.add(scroll);
//
//		
//		// Add the result panel to the frame
//		frame.add(resultPanel, BorderLayout.CENTER);
//		frame.revalidate();
		} catch(SQLException ex){
			throw new RuntimeException(ex);
		}
		
	}
	
	public void putTableInPanel(JTable resultTable, JPanel resultPanel, JFrame frame) {
		resultPanel.removeAll();
		resultPanel.add(new JScrollPane(resultTable));
		

		
		JScrollPane scroll = new JScrollPane(resultTable);
		scroll.setPreferredSize(new Dimension(scrollWidth, scrollHeight));
		resultPanel.add(scroll);

		
		// Add the result panel to the frame
		frame.add(resultPanel, BorderLayout.CENTER);
		frame.revalidate();
	}
	
	public void addEventListenerToTable(JTable resultTable) {
		resultTable.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(final MouseEvent e) {
		        if (e.getClickCount() == 1) {
		            final JTable jTable= (JTable)e.getSource();
		            int row = jTable.getSelectedRow();
		            int column = jTable.getSelectedColumn();
		            String valueInCell = (String)jTable.getValueAt(row, column);
		            System.out.println(valueInCell);
		        }
		    }
		});
	}
	
	
	
}

