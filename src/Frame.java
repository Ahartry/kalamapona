import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URLDecoder;
import java.nio.file.Path;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import org.jfugue.player.Player;

public class Frame {

    ArrayList<Word> wordlist = new ArrayList<>();

    public Frame() throws Exception{
        JFrame frame = new JFrame();
        JPanel panel = new JPanel(new BorderLayout());
        JTextArea textArea = new JTextArea(10, 20);
        JButton button = new JButton("Play");

        JPanel buttonPanel = new JPanel();
        button.setPreferredSize(new Dimension(100, 30));
        buttonPanel.setBackground(Color.LIGHT_GRAY);
        buttonPanel.add(button);

        textArea.setLineWrap(true);
        textArea.setFont(new Font("Sans serif", Font.PLAIN, 20));

        panel.add(textArea, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(panel);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        configLoad();
    
        //String test = "C D E F G A B";
        for(int i = 0; i < wordlist.size(); i++){
            System.out.println(wordlist.get(i).getName());
        }

        button.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) {
                String text = textArea.getText();
                String output = "";
                Scanner scanner = new Scanner(text);
                while(scanner.hasNext()){
                    String word = scanner.next();
                    for(int i = 0; i < wordlist.size(); i++){
                        if(word.equals(wordlist.get(i).getName())){
                            output += wordlist.get(i).getNote();
                            break;
                        }
                    }
                }
                scanner.close();
                new Player().delayPlay(200, output);

            } 
            
        });
    }

    public void configLoad() throws Exception{
        Scanner filescanner;

        String jarDir = getJarContainingFolder(getClass());

        // Create a File object for the current directory
        File directory = new File(jarDir);

        // Get a list of files in the directory
        File[] files = directory.listFiles();

        Path path = Path.of(jarDir + File.separator + "dict.txt");

        boolean configFound = false;
        // Display the list of files
        if (files != null) {
            for (File file : files) {
                if(file.getName().equals("dict.txt")){
                    configFound = true;
                }
            }
        }
        if(!configFound){
            System.out.println("No dict.txt file found");
        }

        File config = path.toFile();

        filescanner = new Scanner(config);

        Scanner stringScanner;

        while(filescanner.hasNext()){
            String line = filescanner.nextLine();
            stringScanner = new Scanner(line);
            String word = stringScanner.next();
            String note = stringScanner.nextLine();
            note += " Ri";
            wordlist.add(new Word(word, note));
        }

        filescanner.close();
    }

    @SuppressWarnings("rawtypes")
    public String getJarContainingFolder(Class aclass) throws Exception {
        CodeSource codeSource = aclass.getProtectionDomain().getCodeSource();
      
        File jarFile;
      
        if (codeSource.getLocation() != null) {
          jarFile = new File(codeSource.getLocation().toURI());
        }
        else {
          String path = aclass.getResource(aclass.getSimpleName() + ".class").getPath();
          String jarFilePath = path.substring(path.indexOf(":") + 1, path.indexOf("!"));
          jarFilePath = URLDecoder.decode(jarFilePath, "UTF-8");
          jarFile = new File(jarFilePath);
        }
        return jarFile.getParentFile().getAbsolutePath();
      }

}
