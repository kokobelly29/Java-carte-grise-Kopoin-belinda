package views;

import javax.swing.*; //boutons par défaut sont rectangulaires
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Fenêtre principale pour naviguer entre les différentes vues.
 */
public class MainView extends JFrame {
    public MainView() {
        setTitle("Gestion des entités");
        setSize(400, 400); // Taille de la fenêtre
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Définir une couleur pour les boutons
        Color rose = new Color(253, 222, 217);

        // Couleur de fond
        Color customBackground = new Color(89, 117, 156); // (R, G, B)

        // Utilisation d'un panneau principal avec BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(customBackground); // Appliquer la couleur de fond personnalisée

        // Panneau pour les boutons avec GridLayout
        JPanel buttonPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        buttonPanel.setOpaque(false); // Rendre le panneau transparent pour voir le fond du panneau principal

        // Ajouter les boutons au panneau
        buttonPanel.add(createButton("Gérer les marques", rose, MarqueView.class));
        buttonPanel.add(createButton("Gérer les modèles", rose, ModeleView.class));
        buttonPanel.add(createButton("Gérer les propriétaires", rose, ProprietaireView.class));
        buttonPanel.add(createButton("Gérer les véhicules", rose, VehiculeView.class));
        buttonPanel.add(createButton("Gérer les propriétés", rose, PossederView.class));

        // Ajouter le panneau de boutons au panneau principal
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        add(mainPanel); // Ajouter le panneau principal à la fenêtre

        setVisible(true); // Rendre la fenêtre visible
    }

    private JButton createButton(String text, Color backgroundColor, Class<?> viewClass) {
        JButton button = new JButton(text);
        button.setBackground(backgroundColor);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    JFrame view = (JFrame) viewClass.getDeclaredConstructor().newInstance();
                    view.setVisible(true);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null,
                            "Erreur lors de l'ouverture de " + text + " : " + ex.getMessage(),
                            "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainView()); // Lancement de l'application sur l'EDT
    }
}
