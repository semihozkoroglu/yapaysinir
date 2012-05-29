/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author sem
 */
import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;

import java.util.Random;
import java.lang.Math;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Stack;
import java.io.*;



class process {
    
    public double net;
    public double cikti;
    
}

public class backpropagation extends javax.swing.JFrame {

    /**
     * Creates new form backpropagation
     */
    public backpropagation() {
        initComponents();
    }
    
    Random generator = new Random();
    
    //Arakatman ile cıktı katmanı sayıları
    int FIXLEN = 1000000;
    int arakatlen, ciktikatlen;
    double[] sigmacikti;
    double[] sigmaara;
    
    double[][] Gate1;
    double[] fi1;
    double[] fi1degisim;
    double[][] Gate1degisim;
    
    double[][] Gate2;
    double[][] Gate2orjinal;
    double[] fi2;
    double[] fi2degisim;
    double[][] Gate2degisim;
    
    double[][] hata;
    //Girdilen örnek sayımız, xsayısı ise girdideki x'ler
    public static int girdilen, xsayisi, index = 0;
    double alfa = 0.5, momentum = 0.8;
    
    String[][] girdi;
    String[][] beklenen;
    process[] arakatman;
    process[] ciktikatman;
    File filegirdi;
    File filebeklenen;
    
    public void ilklendir(){
        int i, j;
        
        Gate1 = new double[xsayisi][arakatlen];
        fi1 = new double[arakatlen];
        fi1degisim = new double[arakatlen];
        Gate1degisim = new double[xsayisi][arakatlen];
        
        Gate2 = new double[arakatlen][ciktikatlen];
        Gate2orjinal = new double[arakatlen][ciktikatlen];
        
        fi2 = new double[ciktikatlen];
        fi2degisim = new double[ciktikatlen];        
        Gate2degisim = new double[arakatlen][ciktikatlen];
        //hatanın gidisini gormek için girdi sayımız kadar yapıyoruz.
        hata = new double[girdilen][ciktikatlen];
        
        arakatman = new process[arakatlen];
        ciktikatman = new process[ciktikatlen];


        for( i = 0; i < xsayisi; i++){           
            for( j = 0; j < arakatlen; j++){
                fi1[j] = generator.nextDouble();
                fi1degisim[j] = 0;                
                Gate1[i][j] = generator.nextDouble();
                Gate1degisim[i][j] = 0;
            }
        }
        for( i = 0; i < arakatlen; i++){
            for( j = 0; j < ciktikatlen; j++){
                fi2[j] = generator.nextDouble();
                fi2degisim[j] = 0;
                Gate2[i][j] = generator.nextDouble();
                Gate2degisim[i][j] = 0;
            }
        }
    }
    public String[][] parse(int alan){
        int i, j, k;
        String[] lines;
        
        if ( alan == 0){
            try{
                fileread.split(filegirdi.toString());
                girdilen = fileread.linecount;
                xsayisi = fileread.all[girdilen-1].length;
            }
            catch (Exception e){
                System.out.print("hatali durum");
            }
        }
        else if( alan == 1){          
            try{
                fileread.split(filebeklenen.toString());
            }
            catch (Exception e){
                System.out.print("hatali durum");
            }            
        }
        else{
            String[][] all;
            lines = jTextField4.getText().split("\n");
            j = lines.length;
            all = new String[lines.length][];
            for( i = 0; i < j; i++){
                all[i] = lines[i].split(" ");
            }
            return all;
        }       
        
        return fileread.all;
    }
    public void gerihesap(){
        ciktiagirlik();
        araagirlik();    
    }
    public void ilerihesap(){
        arasurec();
        ciktisurec();
    }
    public void araagirlik(){
        sigmaara = new double[arakatlen];
        int i, j;
        double toplam;
        
        for ( i = 0; i < arakatlen; i++){
            toplam = 0;
            for ( j = 0; j < ciktikatlen; j++ ){
                toplam += sigmacikti[j]*Gate2orjinal[i][j];
            }            
            sigmaara[i] = arakatman[i].cikti*(1-arakatman[i].cikti)*toplam;
        }
        for( i = 0; i < xsayisi; i++){
            for( j = 0; j < arakatlen; j++){
                Gate1degisim[i][j] = alfa*sigmaara[j]*Double.parseDouble(girdi[index][i]) + momentum*Gate1degisim[i][j];
                Gate1[i][j] += Gate1degisim[i][j];
            }
        }
        for( i = 0; i < arakatlen; i++){
            fi1degisim[i] = alfa*sigmaara[i] + momentum*fi1degisim[i];
            fi1[i] += fi1degisim[i];         
        }
    }
    public void ciktiagirlik(){
        sigmacikti = new double[ciktikatlen];
        int i, j;
        
        for( i = 0; i < ciktikatlen; i++){
            sigmacikti[i] = ciktikatman[i].cikti*(1-ciktikatman[i].cikti)*hata[index][i];
        }
        for( i = 0; i < arakatlen; i++){
            for( j = 0; j < ciktikatlen; j++){
                Gate2degisim[i][j] = alfa*sigmacikti[j]*arakatman[i].cikti + momentum*Gate2degisim[i][j];
                Gate2[i][j] += Gate2degisim[i][j];
            }
        }
        for( j = 0; j < ciktikatlen; j++){
            fi2degisim[j] = alfa*sigmacikti[j] + momentum*fi2degisim[j];
            fi2[j] += fi2degisim[j];
        }
    }
    public void ciktisurec(){
        int j, i;
        double toplam;
        for( j = 0; j < ciktikatlen; j++){
            toplam = 0;
            for( i = 0; i < arakatlen; i++){
                toplam += (arakatman[i].cikti)*(Gate2[i][j]);
            }
            ciktikatman[j] = new process();
            ciktikatman[j].net = toplam + fi2[j];
            ciktikatman[j].cikti = 1/(1 + Math.pow(Math.E,-1*ciktikatman[j].net));
            hata[index][j] = Double.parseDouble(beklenen[index][j]) - ciktikatman[j].cikti;
        }        
    }
    public void arasurec(){
        int j, i;
        double toplam;
        
        for( j = 0; j < arakatlen; j++){
            toplam = 0;
            for( i = 0; i < xsayisi; i++){
                toplam += Double.parseDouble(girdi[index][i])*Gate1[i][j];
            }
            arakatman[j] = new process();
            arakatman[j].net = toplam + fi1[j];
            arakatman[j].cikti = 1/(1 + Math.pow(Math.E,-1*arakatman[j].net));       
        }
    }
    public void guncelleme(){
        
        int i, j;
        
        for ( i = 0; i < arakatlen; i++){
            for (j = 0; j < ciktikatlen; j++){
                Gate2orjinal[i][j] = Gate2[i][j];
            }
        }    
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea3 = new javax.swing.JTextArea();
        jDialog1 = new javax.swing.JDialog();
        jFileChooser1 = new javax.swing.JFileChooser();
        jDialog2 = new javax.swing.JDialog();
        jFileChooser2 = new javax.swing.JFileChooser();
        jButton1 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();

        jTextArea3.setColumns(20);
        jTextArea3.setRows(5);
        jScrollPane3.setViewportView(jTextArea3);

        jFileChooser1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFileChooser1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jDialog1Layout = new javax.swing.GroupLayout(jDialog1.getContentPane());
        jDialog1.getContentPane().setLayout(jDialog1Layout);
        jDialog1Layout.setHorizontalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jFileChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 358, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jDialog1Layout.setVerticalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDialog1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jFileChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 341, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(87, 87, 87))
        );

        jFileChooser2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFileChooser2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jDialog2Layout = new javax.swing.GroupLayout(jDialog2.getContentPane());
        jDialog2.getContentPane().setLayout(jDialog2Layout);
        jDialog2Layout.setHorizontalGroup(
            jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDialog2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jFileChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, 377, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(70, 70, 70))
        );
        jDialog2Layout.setVerticalGroup(
            jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jFileChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, 367, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButton1.setFont(new java.awt.Font("Ubuntu", 1, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(1, 95, 115));
        jButton1.setText("Eğit");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Ubuntu", 1, 14)); // NOI18N
        jButton2.setForeground(new java.awt.Color(1, 95, 115));
        jButton2.setText("Test");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Ubuntu", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(1, 95, 115));
        jLabel1.setText("Arakatman sayısı:");

        jLabel3.setFont(new java.awt.Font("Ubuntu", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(1, 95, 115));
        jLabel3.setText("Hata oranı:");

        jLabel4.setFont(new java.awt.Font("Ubuntu", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(1, 95, 115));
        jLabel4.setText("Girdi:");

        jLabel5.setFont(new java.awt.Font("Ubuntu", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(1, 95, 115));
        jLabel5.setText("Çıktı:");

        jLabel2.setFont(new java.awt.Font("Ubuntu", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 156, 54));

        jLabel6.setFont(new java.awt.Font("Ubuntu", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(1, 95, 115));
        jLabel6.setText("Girdi dosyası:");

        jButton3.setText("Dosya Seç");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Dosya Seç");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Ubuntu", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(1, 95, 115));
        jLabel7.setText("Beklenen dosyası:");

        jLabel8.setFont(new java.awt.Font("Ubuntu", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(1, 95, 115));

        jLabel9.setFont(new java.awt.Font("Ubuntu", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(1, 95, 115));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(44, 44, 44))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(61, 61, 61)
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(158, 158, 158))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(34, 34, 34)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(jLabel5)
                                                .addGap(54, 54, 54)))
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jTextField3)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(0, 57, Short.MAX_VALUE)))
                                        .addGap(25, 25, 25))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel7)
                                        .addGap(30, 30, 30)
                                        .addComponent(jButton4)
                                        .addContainerGap())))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton3)
                                .addGap(32, 339, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(31, 31, 31)
                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE))))))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jButton1, jButton2});

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jTextField1, jTextField2, jTextField4});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3)
                    .addComponent(jButton4)
                    .addComponent(jLabel7))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton1))
                .addGap(24, 24, 24))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel2, jTextField1, jTextField2, jTextField3, jTextField4});

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        int epoch = 0, durum = 0;
        double hataorani, toplamhata = 0;
        
//        String Islem = (String)jComboBox1.getSelectedItem();
        
        beklenen = parse(1);//beklenen
        girdi = parse(0); //girdi

//        GraphingData.data = new double[FIXLEN];
        
        arakatlen = Integer.parseInt(jTextField1.getText().trim());
        hataorani = Double.parseDouble(jTextField2.getText().trim());
        ciktikatlen = beklenen[0].length;
        
        ilklendir();
     
        while(true){

//                GraphingData.data[index] = hata[index][0];
//                System.out.print(hata[index][0]);
            durum += 1;
            ilerihesap();

//            toplamhata += hata[index][0]; 2

            if ( Math.abs(hata[index][0]) > hataorani){
                gerihesap();
                guncelleme();
                durum = 0;
            }

            if (index == (girdilen-1)){
                epoch += 1;
                index = 0;
            }
            else
                index += 1;

            if ( durum == girdilen)
                break;
        }
        jLabel2.setText(Integer.toString(epoch)+ "  " + "Epoch 'da tamamlandı.");
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        int i, j;
        
        girdi = parse(3);// çıktı
        index = 0;
        ilerihesap();

        for( j = 0; j < ciktikatlen; j++){
            jTextField3.setText(Double.toString(ciktikatman[j].cikti));  
        }
   
//        JFrame f = new JFrame();
//        f.add(new GraphingData());
//        f.setSize(400,400);
//        f.setLocation(200,200);
//        f.setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        jDialog1.setBounds(0,0,400,400);
        jDialog1.setVisible(true);        
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jFileChooser1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFileChooser1ActionPerformed
        // TODO add your handling code here:
        filegirdi = jFileChooser1.getSelectedFile();
        jLabel8.setText(filegirdi.getName().toString());
        jDialog1.setVisible(false);        
    }//GEN-LAST:event_jFileChooser1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        jDialog2.setBounds(0,0,400,400);
        jDialog2.setVisible(true);          
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jFileChooser2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFileChooser2ActionPerformed
        // TODO add your handling code here:
        filebeklenen = jFileChooser2.getSelectedFile();
        jLabel9.setText(filebeklenen.getName().toString());
        jDialog2.setVisible(false);          
    }//GEN-LAST:event_jFileChooser2ActionPerformed


    public static void main(String args[]) {

        
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new backpropagation().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JDialog jDialog2;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JFileChooser jFileChooser2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextArea jTextArea3;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    // End of variables declaration//GEN-END:variables
}
