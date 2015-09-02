/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myorg.informes;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "File",
        id = "org.myorg.informes.conectarBaseDatos"
)
@ActionRegistration(
        iconBase = "org/myorg/informes/IconoConectarBD.png",
        displayName = "#CTL_conectarBaseDatos"
)
@ActionReferences({
    @ActionReference(path = "Menu/File", position = 1300),
    @ActionReference(path = "Toolbars/File", position = 300)
})
@Messages("CTL_conectarBaseDatos=Conectar a la base de datos")
public final class conectarBaseDatos implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
         informesTopComponent tc = informesTopComponent.findInstance();
         
         tc.conectarBD();
    }
}
