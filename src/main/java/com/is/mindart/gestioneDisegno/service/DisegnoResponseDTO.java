
package com.is.mindart.gestioneDisegno.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class DisegnoResponseDTO {

    /**Identificativo del disegno
     */
    private Long id;
    /**Identificativo della sessione associata al disegno
    */
    private DrawingDataDTO disegno;

}
