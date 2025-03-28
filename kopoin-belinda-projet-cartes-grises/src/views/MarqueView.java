package views;

import controllers.MarqueController;
import models.Marque;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MarqueView extends JFrame {
    private MarqueController controller;
    private Color backgroundColor = new Color(89, 117, 156); // Couleur de fond RGB(89, 117, 156)
    private Color pinkColor = new Color(255, 192, 203); // Couleur rose pour les boutons
    private Color blackColor = Color.BLACK; // Couleur noire pour le texte

    public MarqueView() {
        controller = new MarqueController();

        setTitle("Liste des Marques");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Appliquer la couleur de fond à la fenêtre
        getContentPane().setBackground(backgroundColor); // Couleur de fond du JFrame
        getContentPane().setLayout(new BorderLayout()); // Utiliser BorderLayout pour le JFrame


        // Panel principal
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(backgroundColor); // Couleur de fond du JPanel
        panel.setOpaque(true); // Assurez-vous que le panneau est opaque

        // Charger les marques
        List<Marque> marques = controller.getAllMarques();
        for (Marque marque : marques) {
            JPanel marquePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            marquePanel.setBackground(backgroundColor); // Couleur de fond du panneau de marque
            marquePanel.setOpaque(true); // Assurez-vous que le panneau est opaque

            JLabel marqueLabel = new JLabel(marque.getNomMarque());
            marqueLabel.setForeground(blackColor); // Appliquer la couleur noire au texte

            JButton modifyButton = new JButton("Modifier");
            modifyButton.setBackground(pinkColor); // Appliquer la couleur rose au bouton
            modifyButton.setForeground(Color.WHITE); // Couleur du texte en blanc
            modifyButton.addActionListener(e -> {
                String newName = JOptionPane.showInputDialog(this, "Nouveau nom :", marque.getNomMarque());
                if (newName != null && !newName.isEmpty()) {
                    controller.updateMarque(marque.getIdMarque(), newName);
                    refreshView();
                }
            });

            JButton deleteButton = new JButton("Supprimer");
            deleteButton.setBackground(pinkColor); // Appliquer la couleur rose au bouton
            deleteButton.setForeground(Color.WHITE); // Couleur du texte en blanc
            deleteButton.addActionListener(e -> {
                controller.deleteMarque(marque.getIdMarque());
                refreshView();
            });

            marquePanel.add(marqueLabel);
            marquePanel.add(modifyButton);
            marquePanel.add(deleteButton);
            panel.add(marquePanel);
        }

        // Panel pour les boutons "Ajouter" et "Retour"
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(backgroundColor); // Couleur de fond du panneau de boutons
        buttonPanel.setOpaque(true); // Assurez-vous que le panneau est opaque

        JButton addButton = new JButton("Ajouter une marque");
        addButton.setBackground(pinkColor); // Appliquer la couleur rose au bouton
        addButton.setForeground(Color.WHITE); // Couleur du texte en blanc
        addButton.addActionListener(e -> {
            String newName = JOptionPane.showInputDialog(this, "Nom de la nouvelle marque :");
            if (newName != null && !newName.isEmpty()) {
                controller.addMarque(newName);
                refreshView();
            }
        });

        JButton backButton = new JButton("Retour");
        backButton.setBackground(pinkColor); // Appliquer la couleur rose au bouton
        backButton.setForeground(Color.WHITE); // Couleur du texte en blanc
        backButton.addActionListener(e -> dispose());

        buttonPanel.add(addButton);
        buttonPanel.add(backButton);

        panel.add(buttonPanel);

        // Scroller
        JScrollPane scrollPane = new JScrollPane(panel);
        add(scrollPane, BorderLayout.CENTER); // Ajouter le panneau principal au centre

        setVisible(true);
    }


    private void refreshView() {
        dispose();
        new MarqueView();
    }
}
