import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


class MainScreen extends JFrame implements ActionListener{

    JButton button;
    JTextField delimiter_input;
    JLabel intro_text = new JLabel("First input the delimiter and then select the file.", SwingConstants.CENTER);
    JTable tb = new JTable();
    JScrollPane sp = new JScrollPane(tb);
    JPanel input_panel = new JPanel();

    MainScreen(){

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();


        this.setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));

        button = new JButton("Select File");
        delimiter_input = new JTextField(";", 1);
        button.addActionListener(this);


        this.add(intro_text);
        this.add(input_panel);
        input_panel.add(button);
        input_panel.add(delimiter_input);

        this.setVisible(true);

        this.pack();
        this.setSize(400, 100);

    }



    @Override

    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==button) {
            JFileChooser fileChooser = new JFileChooser();

            fileChooser.setCurrentDirectory(new File(".")); //sets current directory

            int response = fileChooser.showOpenDialog(null); //select file to open

            if(response == JFileChooser.APPROVE_OPTION) {
                File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                System.out.println(file);

                String[][] data = CSVReader(file.toString(), delimiter_input.getText());

                String[] columnNames = data[0]; // data first row has header

                String[][] noHeaderData = Arrays.copyOfRange(data, 1, data.length);


                this.remove(sp); // remove existing scroll pane, including the existing table, if it even exists
                this.remove(input_panel);
                input_panel.add(button); // so that it comes after the table
                input_panel.add(delimiter_input);
                this.add(input_panel);
                tb = new JTable(noHeaderData, columnNames);
                sp = new JScrollPane(tb);

                this.add(sp);
            }

            this.setSize(1000, 701);
        }
    }

    public String[][] CSVReader(String filename, String delimiter){
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));


            int numOfLines = getLines(filename);
            String line = br.readLine();
            String[][] rows = new String[numOfLines][];
            int lineNr = 0;
            while(line!=null) {
                rows[lineNr] = line.split(delimiter);
                line = br.readLine();
                lineNr++;
            }

            return rows;
        }catch(Exception e){
            System.out.println(e);
        }
        return new String[0][]; //empty row if unsuccessful
    }

    private int getLines(String filename) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        int lines = 0;
        while (br.readLine() != null) lines++;

        br.close();
        return lines;
    }


}