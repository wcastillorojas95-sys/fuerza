package com.lucas.fuerza

/**
 * A que grupo muscular pertenece cada ejercicio.
 *
 * Doce grupos y no mas. Se puede hilar mas fino -- separar deltoides anterior de
 * posterior, dorsal de romboides -- pero para decidir que entrenas hoy no sirve
 * de nada y llena los filtros de ruido.
 */
enum class Musculo(val etiqueta: String) {
    PECHO("Pecho"),
    ESPALDA("Espalda"),
    HOMBRO("Hombro"),
    BICEPS("Biceps"),
    TRICEPS("Triceps"),
    CUADRICEPS("Cuadriceps"),
    FEMORAL("Femoral"),
    GLUTEO("Gluteo"),
    GEMELO("Gemelo"),
    CORE("Core"),
    ANTEBRAZO("Antebrazo"),
    TRAPECIO("Trapecio")
}

/**
 * Cuanta tecnica pide el ejercicio, no cuanto cansa.
 *
 * Un curl de biceps con veinte kilos cansa mas que una sentadilla frontal con
 * la barra vacia, y aun asi el curl es de principiante y la frontal no: lo que
 * mide esto es cuanto puedes hacerte dano haciendolo mal y cuanto tiempo hace
 * falta para que el movimiento salga solo.
 */
enum class Dificultad(val etiqueta: String) {
    PRINCIPIANTE("Principiante"),
    INTERMEDIO("Intermedio"),
    AVANZADO("Avanzado")
}

enum class Equipo(val etiqueta: String) {
    BARRA("Barra"),
    MANCUERNA("Mancuerna"),
    POLEA("Polea"),
    MAQUINA("Maquina"),
    BANDA("Banda elastica"),
    CORPORAL("Peso corporal"),
    KETTLEBELL("Kettlebell")
}

/**
 * Un ejercicio del catalogo.
 *
 * [claves] son dos frases de tecnica, no un tutorial. La idea es lo que
 * necesitas leer de reojo entre serie y serie, no un curso: para aprender un
 * movimiento nuevo hace falta un video o alguien que te mire, y esta app no
 * pretende sustituir ni lo uno ni lo otro.
 *
 * [compuesto] separa lo que mueve varias articulaciones de lo que mueve una. Es
 * lo que usa el generador de rutinas para poner primero lo pesado.
 *
 * [dificultad] va sin valor por defecto a proposito: asi el compilador obliga a
 * decidirla en cada ejercicio nuevo en vez de dejarla caer en "intermedio" sin
 * que nadie lo mire.
 */
data class Ejercicio(
    val id: String,
    val nombre: String,
    val musculo: Musculo,
    val secundarios: List<Musculo>,
    val equipo: Equipo,
    val compuesto: Boolean,
    val claves: String,
    val dificultad: Dificultad,
    /**
     * Los pasos del movimiento, en orden.
     *
     * Vacio en los que todavia no los tienen. No es lo mismo que [claves]:
     * las claves son los dos errores que hay que evitar cuando ya sabes hacerlo,
     * los pasos son como se hace desde cero.
     */
    val pasos: List<String> = emptyList()
)

/**
 * El catalogo entero, compilado dentro del APK.
 *
 * Va aqui y no en un JSON de assets, ni en una API, por tres razones: el
 * compilador comprueba que no hay ningun id mal escrito, funciona sin cobertura
 * en el sotano del gimnasio, y no depende de que un servicio ajeno siga
 * existiendo dentro de dos anos.
 *
 * Los nombres de los ejercicios y los musculos que trabajan son hechos, no obra
 * de nadie. Las descripciones estan escritas para esta app.
 */
val CATALOGO: List<Ejercicio> = listOf(
    Ejercicio(
        "press_banca", "Press de banca",
        Musculo.PECHO, listOf(Musculo.TRICEPS, Musculo.HOMBRO), Equipo.BARRA, true,
        "Escapulas metidas y pegadas al banco, pies clavados en el suelo. La barra baja al esternon, no al cuello, y sube en diagonal ligera hacia la cara.",
        Dificultad.INTERMEDIO,
        pasos = listOf(
            "Tumbate con los ojos debajo de la barra, los pies firmes y los omoplatos juntos contra el banco.",
            "Agarra la barra un poco mas ancho que los hombros y sacala del soporte con los brazos extendidos.",
            "Bajala controlando hacia la zona media o inferior del pecho, con los codos a unos 45 grados del torso.",
            "Empuja la barra hacia arriba hasta extender los brazos sin despegar los gluteos ni perder la posicion de los hombros."
        )
    ),
    Ejercicio(
        "press_suelo_mancuernas", "Press de suelo con mancuernas",
        Musculo.PECHO, listOf(Musculo.TRICEPS, Musculo.HOMBRO), Equipo.MANCUERNA, true,
        "El suelo corta el recorrido antes de que el hombro se vaya demasiado atras. Pausa al apoyar los brazos: no rebotes los codos contra el piso.",
        Dificultad.PRINCIPIANTE,
        pasos = listOf(
            "Sientate en el suelo con las mancuernas sobre los muslos y recuestate con las rodillas flexionadas.",
            "Coloca las mancuernas a los lados del pecho y manten los antebrazos verticales.",
            "Baja despacio hasta que los triceps contacten suavemente con el suelo y haz una pausa breve.",
            "Empuja las mancuernas hasta extender los brazos y repite sin perder el control."
        )
    ),
    Ejercicio(
        "press_banca_inclinado", "Press inclinado con barra",
        Musculo.PECHO, listOf(Musculo.HOMBRO, Musculo.TRICEPS), Equipo.BARRA, true,
        "Banco entre 30 y 45 grados; mas inclinacion y el trabajo se va al hombro. Baja a la clavicula.",
        Dificultad.INTERMEDIO
    ),
    Ejercicio(
        "press_banca_declinado", "Press declinado con barra",
        Musculo.PECHO, listOf(Musculo.TRICEPS), Equipo.BARRA, true,
        "Pega mas al pectoral bajo. Baja al final de las costillas y no bloquees del todo arriba.",
        Dificultad.INTERMEDIO
    ),
    Ejercicio(
        "press_mancuernas", "Press de banca con mancuernas",
        Musculo.PECHO, listOf(Musculo.TRICEPS, Musculo.HOMBRO), Equipo.MANCUERNA, true,
        "Mas recorrido que con barra y cada lado carga lo suyo. Abajo, los codos no pasan mucho de la linea del torso.",
        Dificultad.INTERMEDIO
    ),
    Ejercicio(
        "press_incl_mancuernas", "Press inclinado con mancuernas",
        Musculo.PECHO, listOf(Musculo.HOMBRO, Musculo.TRICEPS), Equipo.MANCUERNA, true,
        "Banco entre 30 y 45 grados para cargar el pectoral superior sin convertirlo en un press de hombros. Junta las mancuernas arriba sin chocarlas.",
        Dificultad.INTERMEDIO,
        pasos = listOf(
            "Ajusta el banco entre 30 y 45 grados y apoya cabeza, espalda y gluteos.",
            "Empieza con las mancuernas a los lados de la parte superior del pecho y los pies firmes en el suelo.",
            "Empuja hacia arriba y ligeramente hacia el centro sin golpear las mancuernas.",
            "Baja despacio hasta recuperar un estiramiento comodo y repite."
        )
    ),
    Ejercicio(
        "flexiones_declinadas", "Flexiones declinadas",
        Musculo.PECHO, listOf(Musculo.HOMBRO, Musculo.TRICEPS, Musculo.CORE), Equipo.CORPORAL, true,
        "Pies sobre una superficie firme y cuerpo en bloque. Cuanto mas altos estan los pies, mas trabajo se desplaza del pecho superior hacia los hombros.",
        Dificultad.PRINCIPIANTE,
        pasos = listOf(
            "Apoya los pies en un banco, cajon o escalon estable y coloca las manos algo mas anchas que los hombros.",
            "Aprieta abdomen y gluteos para formar una linea recta desde la cabeza hasta los talones.",
            "Baja el pecho hacia el suelo con los codos en diagonal, no abiertos en cruz.",
            "Empuja el suelo hasta volver arriba sin dejar caer la cadera."
        )
    ),
    Ejercicio(
        "aperturas_mancuerna", "Aperturas con mancuernas",
        Musculo.PECHO, emptyList(), Equipo.MANCUERNA, false,
        "Codos semiflexionados y fijos todo el rato: si se abren y cierran, es un press. Abre hasta notar el estiramiento, no mas.",
        Dificultad.PRINCIPIANTE
    ),
    Ejercicio(
        "aperturas_polea", "Cruce de poleas",
        Musculo.PECHO, emptyList(), Equipo.POLEA, false,
        "La polea mantiene tension tambien al juntar las manos, donde la mancuerna la pierde. Los codos conservan la misma flexion: es una apertura, no un press.",
        Dificultad.PRINCIPIANTE,
        pasos = listOf(
            "Coloca las poleas a la altura de los hombros o del pecho y toma una agarradera con cada mano.",
            "Da un paso al frente, inclina apenas el torso y deja los codos ligeramente flexionados.",
            "Acerca las manos delante del cuerpo mediante un arco y aprieta el pecho un instante.",
            "Abre los brazos despacio hasta sentir un estiramiento comodo, sin llevarlos demasiado atras."
        )
    ),
    Ejercicio(
        "aperturas_banda_unilateral", "Apertura unilateral con banda",
        Musculo.PECHO, listOf(Musculo.HOMBRO), Equipo.BANDA, false,
        "Ancla la banda a la altura del hombro y mueve el brazo en arco. El torso no gira: si necesitas retorcerte, la banda ofrece demasiada resistencia.",
        Dificultad.PRINCIPIANTE,
        pasos = listOf(
            "Fija la banda a la altura del hombro y colocate de lado al anclaje con una base estable.",
            "Sujeta la banda con el brazo abierto y el codo ligeramente flexionado.",
            "Lleva la mano en arco hasta cruzarla delante del pecho sin girar el tronco.",
            "Regresa lentamente y completa todas las repeticiones antes de cambiar de lado."
        )
    ),
    Ejercicio(
        "press_maquina_pecho", "Press declinado en maquina",
        Musculo.PECHO, listOf(Musculo.TRICEPS), Equipo.MAQUINA, true,
        "Ajusta el asiento para que las agarraderas queden frente a la zona media o inferior del pecho. La espalda permanece apoyada durante todo el recorrido.",
        Dificultad.PRINCIPIANTE,
        pasos = listOf(
            "Carga la maquina y ajusta el asiento para alcanzar las agarraderas sin adelantar los hombros.",
            "Apoya espalda y cabeza, planta los pies y toma las agarraderas con firmeza.",
            "Empuja hacia delante hasta extender los brazos sin bloquear los codos con violencia.",
            "Vuelve despacio hasta un estiramiento comodo y repite sin despegar la espalda."
        )
    ),
    Ejercicio(
        "press_pecho_banda", "Press de pecho con banda",
        Musculo.PECHO, listOf(Musculo.TRICEPS, Musculo.HOMBRO), Equipo.BANDA, true,
        "Comprueba la banda y el anclaje antes de cada serie. Alejate para sumar tension, pero no tanto como para perder el control del regreso.",
        Dificultad.PRINCIPIANTE,
        pasos = listOf(
            "Ancla la banda detras de ti, aproximadamente a la altura del pecho.",
            "Sujeta los extremos, adelanta un pie y manten el abdomen firme.",
            "Empuja las manos al frente hasta extender los brazos sin encoger los hombros.",
            "Regresa lentamente hasta que las manos vuelvan junto al pecho."
        )
    ),
    Ejercicio(
        "peck_deck", "Contractor de pecho (peck deck)",
        Musculo.PECHO, emptyList(), Equipo.MAQUINA, false,
        "Aislamiento puro. Aprieta un segundo al juntar y vuelve despacio.",
        Dificultad.PRINCIPIANTE
    ),
    Ejercicio(
        "fondos_pecho", "Fondos en paralelas",
        Musculo.PECHO, listOf(Musculo.TRICEPS, Musculo.HOMBRO), Equipo.CORPORAL, true,
        "Inclinate hacia delante para cargar pectoral; vertical carga triceps. Baja hasta que el hombro quede a la altura del codo y ni un dedo mas.",
        Dificultad.INTERMEDIO,
        pasos = listOf(
            "Sujeta las barras y empieza arriba con los brazos extendidos y los hombros lejos de las orejas.",
            "Inclina ligeramente el torso hacia delante y lleva las piernas un poco atras.",
            "Baja controlando hasta que el brazo quede aproximadamente paralelo al suelo o antes si el hombro molesta.",
            "Empuja las barras hasta volver a la posicion inicial sin balancearte."
        )
    ),
    Ejercicio(
        "flexiones_diamante_peso", "Flexiones diamante con peso",
        Musculo.PECHO, listOf(Musculo.TRICEPS, Musculo.HOMBRO, Musculo.CORE), Equipo.CORPORAL, true,
        "Esta variante carga mas los triceps que los fondos y no los copia exactamente. El peso debe quedar estable sobre la espalda alta; si entrenas solo, hazla sin disco o usa banda.",
        Dificultad.INTERMEDIO,
        pasos = listOf(
            "Coloca las manos juntas debajo del pecho formando un diamante con los dedos y manten el cuerpo alineado.",
            "Pide a otra persona que coloque y estabilice el peso sobre la parte alta de la espalda.",
            "Baja el pecho hacia las manos con los codos cerca del cuerpo.",
            "Empuja hasta extender los brazos sin dejar que la cadera se hunda."
        )
    ),
    Ejercicio(
        "flexiones", "Flexiones",
        Musculo.PECHO, listOf(Musculo.TRICEPS, Musculo.CORE), Equipo.CORPORAL, true,
        "El cuerpo es una tabla de la cabeza a los talones. Codos a unos 45 grados del torso, no abiertos en cruz.",
        Dificultad.PRINCIPIANTE,
        pasos = listOf(
            "Coloca las manos ligeramente mas anchas que los hombros y estira las piernas hacia atras.",
            "Aprieta abdomen y gluteos para mantener cabeza, espalda, cadera y piernas alineadas.",
            "Baja el pecho hacia el suelo con los codos a unos 45 grados del torso.",
            "Empuja el suelo hasta volver arriba sin perder la linea del cuerpo."
        )
    ),
    Ejercicio(
        "flexiones_banda", "Flexiones con banda",
        Musculo.PECHO, listOf(Musculo.TRICEPS, Musculo.HOMBRO, Musculo.CORE), Equipo.BANDA, true,
        "La banda pasa por la espalda alta, nunca por el cuello, y queda atrapada debajo de las manos. Empieza con poca resistencia para que no se mueva.",
        Dificultad.INTERMEDIO,
        pasos = listOf(
            "Pasa la banda por la parte alta de la espalda y sujeta cada extremo debajo de una mano.",
            "Adopta la posicion de flexion con el cuerpo recto y las manos algo mas anchas que los hombros.",
            "Baja el pecho controlando mientras mantienes los codos en diagonal.",
            "Empuja contra el suelo y la banda hasta extender los brazos."
        )
    ),
    Ejercicio(
        "pullover", "Pullover con mancuerna",
        Musculo.PECHO, listOf(Musculo.ESPALDA, Musculo.CORE), Equipo.MANCUERNA, false,
        "Tumbado, la mancuerna baja por detras de la cabeza con los codos casi rectos. Abre caja toracica y toca dorsal a la vez.",
        Dificultad.INTERMEDIO
    ),
    Ejercicio(
        "dominadas", "Dominadas",
        Musculo.ESPALDA, listOf(Musculo.BICEPS, Musculo.ANTEBRAZO), Equipo.CORPORAL, true,
        "Empieza el tiron bajando los hombros, no doblando el codo. Sube hasta pasar la barbilla y baja controlando, sin caer en peso muerto.",
        Dificultad.INTERMEDIO
    ),
    Ejercicio(
        "dominadas_supinas", "Dominadas supinas",
        Musculo.ESPALDA, listOf(Musculo.BICEPS), Equipo.CORPORAL, true,
        "Palmas hacia ti: entra mucho mas biceps y suele salir alguna repeticion mas que en pronacion.",
        Dificultad.INTERMEDIO
    ),
    Ejercicio(
        "jalon_pecho", "Jalon al pecho",
        Musculo.ESPALDA, listOf(Musculo.BICEPS), Equipo.POLEA, true,
        "La barra baja a la clavicula con el pecho sacado. Nada de tirar por detras de la nuca: no aporta y castiga el hombro.",
        Dificultad.PRINCIPIANTE
    ),
    Ejercicio(
        "jalon_agarre_neutro", "Jalon con agarre neutro",
        Musculo.ESPALDA, listOf(Musculo.BICEPS), Equipo.POLEA, true,
        "Manos enfrentadas. Es el agarre mas amable con el hombro y el que mas dorsal bajo pilla.",
        Dificultad.PRINCIPIANTE
    ),
    Ejercicio(
        "remo_barra", "Remo con barra",
        Musculo.ESPALDA, listOf(Musculo.BICEPS, Musculo.FEMORAL), Equipo.BARRA, true,
        "Torso a unos 45 grados y espalda plana. La barra va al ombligo, no al pecho, y el tiron sale de los codos.",
        Dificultad.INTERMEDIO,
        pasos = listOf(
            "Agarra la barra al ancho de los hombros, con las palmas hacia abajo o hacia arriba.",
            "Inclina el torso desde la cadera y manten la espalda recta.",
            "Lleva la barra hacia la parte alta del abdomen.",
            "Baja el peso controlando y repite."
        )
    ),
    Ejercicio(
        "remo_pendlay", "Remo Pendlay",
        Musculo.ESPALDA, listOf(Musculo.TRAPECIO, Musculo.BICEPS), Equipo.BARRA, true,
        "Torso paralelo al suelo y la barra se apoya en el suelo en cada repeticion. Explosivo al subir, estricto en la posicion.",
        Dificultad.AVANZADO
    ),
    Ejercicio(
        "remo_mancuerna", "Remo con mancuerna a una mano",
        Musculo.ESPALDA, listOf(Musculo.BICEPS), Equipo.MANCUERNA, true,
        "Una rodilla en el banco, espalda plana. Lleva el codo hacia la cadera y deja que la escapula viaje al final.",
        Dificultad.INTERMEDIO
    ),
    Ejercicio(
        "remo_sentado_polea", "Remo sentado en polea",
        Musculo.ESPALDA, listOf(Musculo.BICEPS), Equipo.POLEA, true,
        "Rodillas algo flexionadas y torso quieto: si te balanceas hacia atras, sobra peso. Saca pecho al recoger.",
        Dificultad.PRINCIPIANTE,
        pasos = listOf(
            "Sientate en la maquina con la espalda recta y sujeta las asas.",
            "Torso erguido, pecho sacado y las piernas mas o menos a noventa grados del cuerpo.",
            "Lleva las asas hacia atras hasta que las manos queden cerca del abdomen.",
            "Estira los brazos despacio para volver al principio."
        )
    ),
    Ejercicio(
        "remo_maquina", "Remo en maquina",
        Musculo.ESPALDA, listOf(Musculo.BICEPS), Equipo.MAQUINA, true,
        "Pecho apoyado, que quita a la lumbar del medio. Ideal para acumular volumen sin castigar la espalda baja.",
        Dificultad.PRINCIPIANTE
    ),
    Ejercicio(
        "remo_t", "Remo en T",
        Musculo.ESPALDA, listOf(Musculo.TRAPECIO, Musculo.BICEPS), Equipo.BARRA, true,
        "Buen equilibrio entre dorsal y espalda media. Agarre neutro y recorrido completo.",
        Dificultad.INTERMEDIO
    ),
    Ejercicio(
        "peso_muerto", "Peso muerto",
        Musculo.ESPALDA, listOf(Musculo.FEMORAL, Musculo.GLUTEO, Musculo.TRAPECIO), Equipo.BARRA, true,
        "La barra roza la espinilla todo el camino. Empuja el suelo con los pies en vez de tirar con la espalda; el bloqueo es de cadera, no de lumbar.",
        Dificultad.AVANZADO
    ),
    Ejercicio(
        "peso_muerto_rumano", "Peso muerto rumano",
        Musculo.FEMORAL, listOf(Musculo.GLUTEO, Musculo.ESPALDA), Equipo.BARRA, true,
        "Rodillas casi rectas y la cadera va hacia atras. Baja hasta notar el femoral, no hasta tocar el suelo.",
        Dificultad.INTERMEDIO
    ),
    Ejercicio(
        "pullover_polea", "Pullover en polea alta",
        Musculo.ESPALDA, emptyList(), Equipo.POLEA, false,
        "Codos rectos, la barra baja en arco hasta los muslos. Es de lo poco que aisla dorsal sin meter biceps.",
        Dificultad.PRINCIPIANTE
    ),
    Ejercicio(
        "face_pull", "Face pull",
        Musculo.HOMBRO, listOf(Musculo.TRAPECIO, Musculo.ESPALDA), Equipo.POLEA, false,
        "Polea a la altura de la cara, tira de la cuerda separando las manos hacia las orejas. El mejor seguro barato para hombros que hacen mucho press.",
        Dificultad.PRINCIPIANTE
    ),
    Ejercicio(
        "encogimientos", "Encogimientos de hombros",
        Musculo.TRAPECIO, listOf(Musculo.ANTEBRAZO), Equipo.MANCUERNA, false,
        "Sube recto, sin rodar el hombro hacia atras. Aguanta arriba un segundo.",
        Dificultad.PRINCIPIANTE
    ),
    Ejercicio(
        "hiperextensiones", "Hiperextensiones",
        Musculo.ESPALDA, listOf(Musculo.GLUTEO, Musculo.FEMORAL), Equipo.CORPORAL, false,
        "Sube hasta la linea del cuerpo y para: pasarse de ahi solo comprime la lumbar.",
        Dificultad.PRINCIPIANTE
    ),
    Ejercicio(
        "buenos_dias", "Buenos dias",
        Musculo.FEMORAL, listOf(Musculo.ESPALDA, Musculo.GLUTEO), Equipo.BARRA, true,
        "Poco peso y mucha tecnica. La barra alta en la espalda, cadera atras y espalda plana como una mesa.",
        Dificultad.INTERMEDIO
    ),
    Ejercicio(
        "press_militar", "Press militar con barra",
        Musculo.HOMBRO, listOf(Musculo.TRICEPS, Musculo.CORE), Equipo.BARRA, true,
        "De pie, gluteo y abdomen apretados para no arquear la lumbar. La cabeza se retira un poco para dejar pasar la barra.",
        Dificultad.INTERMEDIO
    ),
    Ejercicio(
        "press_hombro_mancuernas", "Press de hombro con mancuernas",
        Musculo.HOMBRO, listOf(Musculo.TRICEPS), Equipo.MANCUERNA, true,
        "Sentado con respaldo si te cuesta mantener la lumbar. Codos ligeramente adelantados, no en cruz.",
        Dificultad.INTERMEDIO
    ),
    Ejercicio(
        "press_arnold", "Press Arnold",
        Musculo.HOMBRO, listOf(Musculo.TRICEPS), Equipo.MANCUERNA, true,
        "Empieza con palmas hacia ti y gira al subir. Pilla deltoides anterior y medio en el mismo viaje.",
        Dificultad.INTERMEDIO
    ),
    Ejercicio(
        "elevaciones_laterales", "Elevaciones laterales",
        Musculo.HOMBRO, emptyList(), Equipo.MANCUERNA, false,
        "El ejercicio que ensancha. Sube hasta la horizontal guiando con el codo, no con la mano, y baja en tres tiempos.",
        Dificultad.PRINCIPIANTE
    ),
    Ejercicio(
        "elevaciones_lateral_polea", "Elevacion lateral en polea",
        Musculo.HOMBRO, emptyList(), Equipo.POLEA, false,
        "La polea da tension desde el primer grado, que es justo donde la mancuerna no da nada.",
        Dificultad.PRINCIPIANTE
    ),
    Ejercicio(
        "elevaciones_frontales", "Elevaciones frontales",
        Musculo.HOMBRO, emptyList(), Equipo.MANCUERNA, false,
        "Suele sobrar si haces press: el deltoides anterior ya trabaja de mas. Uselo con moderacion.",
        Dificultad.PRINCIPIANTE
    ),
    Ejercicio(
        "pajaros", "Pajaros (deltoides posterior)",
        Musculo.HOMBRO, listOf(Musculo.ESPALDA), Equipo.MANCUERNA, false,
        "Torso casi paralelo al suelo. Abre con los codos, no con las manos, y no cojas peso: aqui nadie es fuerte.",
        Dificultad.INTERMEDIO
    ),
    Ejercicio(
        "press_hombro_maquina", "Press de hombro en maquina",
        Musculo.HOMBRO, listOf(Musculo.TRICEPS), Equipo.MAQUINA, true,
        "Trayectoria fija, ideal para las ultimas series cuando el equilibrio ya falla.",
        Dificultad.PRINCIPIANTE
    ),
    Ejercicio(
        "remo_menton", "Remo al menton",
        Musculo.HOMBRO, listOf(Musculo.TRAPECIO), Equipo.BARRA, true,
        "Agarre ancho y sube solo hasta el pecho. Agarre estrecho y subir hasta la barbilla pinza el hombro.",
        Dificultad.INTERMEDIO
    ),
    Ejercicio(
        "curl_barra", "Curl con barra",
        Musculo.BICEPS, listOf(Musculo.ANTEBRAZO), Equipo.BARRA, false,
        "Codos pegados al costado y quietos. Si la cadera acompana el movimiento, es que hay demasiado disco.",
        Dificultad.PRINCIPIANTE
    ),
    Ejercicio(
        "curl_mancuernas", "Curl con mancuernas",
        Musculo.BICEPS, listOf(Musculo.ANTEBRAZO), Equipo.MANCUERNA, false,
        "Gira la muneca hacia fuera al subir: el biceps tambien supina, y asi hace su trabajo completo.",
        Dificultad.PRINCIPIANTE,
        pasos = listOf(
            "De pie y erguido, una mancuerna en cada mano con los brazos estirados a los lados.",
            "Sube una mancuerna girando el antebrazo hasta dejarlo vertical, con la palma mirando al hombro.",
            "Baja a la posicion de partida y repite con el otro brazo."
        )
    ),
    Ejercicio(
        "curl_martillo", "Curl martillo",
        Musculo.BICEPS, listOf(Musculo.ANTEBRAZO), Equipo.MANCUERNA, false,
        "Palmas enfrentadas. Es el que engorda el braquial, que es el musculo que empuja el biceps hacia arriba.",
        Dificultad.PRINCIPIANTE,
        pasos = listOf(
            "Sujeta las mancuernas con agarre neutro, los pulgares hacia arriba.",
            "Flexiona los codos y sube las mancuernas despacio hasta la altura del pecho.",
            "Vuelve controlando a la posicion de partida y repite."
        )
    ),
    Ejercicio(
        "curl_predicador", "Curl en banco predicador",
        Musculo.BICEPS, emptyList(), Equipo.BARRA, false,
        "El banco impide hacer trampa con el hombro. No estires del todo abajo si tienes el codo delicado.",
        Dificultad.PRINCIPIANTE
    ),
    Ejercicio(
        "curl_inclinado", "Curl inclinado en banco",
        Musculo.BICEPS, emptyList(), Equipo.MANCUERNA, false,
        "El brazo cae por detras del cuerpo y estira el biceps al maximo. Duele bien.",
        Dificultad.PRINCIPIANTE
    ),
    Ejercicio(
        "curl_polea", "Curl en polea baja",
        Musculo.BICEPS, emptyList(), Equipo.POLEA, false,
        "Tension constante de abajo arriba. Va muy bien como ultimo ejercicio del dia de tiron.",
        Dificultad.PRINCIPIANTE
    ),
    Ejercicio(
        "curl_concentrado", "Curl concentrado",
        Musculo.BICEPS, emptyList(), Equipo.MANCUERNA, false,
        "Codo apoyado en el muslo. Nada de peso, todo de contraccion.",
        Dificultad.PRINCIPIANTE,
        pasos = listOf(
            "Sientate en un banco con las piernas separadas.",
            "Apoya el brazo en la cara interna del muslo y deja el peso colgando hacia el suelo.",
            "Flexiona el codo y sube hasta que la palma mire al hombro.",
            "Baja controlando y repite."
        )
    ),
    Ejercicio(
        "curl_inverso_mancuernas", "Curl inverso con mancuernas",
        Musculo.BICEPS, listOf(Musculo.ANTEBRAZO), Equipo.MANCUERNA, false,
        "Agarre prono, palmas hacia abajo. Vas a mover bastante menos peso que en un curl normal y es lo normal: aqui el que manda es el braquiorradial, no el biceps.",
        Dificultad.PRINCIPIANTE,
        pasos = listOf(
            "Sujeta las mancuernas con las palmas hacia abajo. Si las munecas se quejan, mete el pulgar junto al resto de dedos.",
            "Flexiona los codos hasta acercar los antebrazos a los biceps, sin dejar que los codos se abran hacia los lados.",
            "Baja controlando hasta estirar los brazos del todo."
        )
    ),
    Ejercicio(
        "curl_giratorio_polea", "Curl giratorio en polea",
        Musculo.BICEPS, listOf(Musculo.ANTEBRAZO), Equipo.POLEA, false,
        "Gira la mano mientras subes, no antes de empezar: el biceps tambien supina, y girando a la vez que flexionas hace sus dos trabajos en el mismo recorrido.",
        Dificultad.AVANZADO,
        pasos = listOf(
            "Pon un mango individual en lo mas bajo de la polea.",
            "Colocate de espaldas a la maquina y adelanta un pie para tener base.",
            "Empieza con agarre neutro. Flexiona el codo girando la mano durante el camino, y termina con la palma hacia arriba.",
            "Estira el codo despacio para volver al principio."
        )
    ),
    Ejercicio(
        "curl_invertido_barra", "Curl invertido con barra",
        Musculo.BICEPS, listOf(Musculo.ANTEBRAZO), Equipo.BARRA, false,
        "Palmas hacia abajo y codos pegados al costado. Usa la mitad del peso que en un curl normal: aqui el eslabon debil son las munecas y no avisan hasta que duelen.",
        Dificultad.AVANZADO,
        pasos = listOf(
            "Sujeta la barra con las palmas hacia abajo, las manos al ancho de los hombros.",
            "Manten los codos cerca del cuerpo y no dejes que se vayan hacia los lados.",
            "Flexiona los brazos hasta acercar los antebrazos a los biceps.",
            "Baja la barra controlando hasta estirar los codos del todo."
        )
    ),
    Ejercicio(
        "curl_polea_alta", "Curl en polea alta",
        Musculo.BICEPS, emptyList(), Equipo.POLEA, false,
        "De lado a la polea alta, palma hacia arriba y el codo hacia las costillas. Trabaja el biceps acortado del todo, que es donde el curl de siempre ya no llega.",
        Dificultad.PRINCIPIANTE,
        pasos = listOf(
            "Pon un mango en lo mas alto de la polea.",
            "Alejate un par de pasos y ponte de lado a la maquina.",
            "Palma hacia arriba, acerca el codo a las costillas.",
            "Vuelve despacio al principio y repite."
        )
    ),
    Ejercicio(
        "curl_bayesian", "Curl bayesiano en polea",
        Musculo.BICEPS, emptyList(), Equipo.POLEA, false,
        "De espaldas a la polea el brazo cae por detras del cuerpo, y ahi el biceps sale estirado del todo. Es la parte del recorrido que mas hipertrofia da.",
        Dificultad.PRINCIPIANTE,
        pasos = listOf(
            "Pon los mangos y baja los cables a lo mas bajo.",
            "Colocate de espaldas a la maquina.",
            "Un pie ligeramente delante del otro, para tener base.",
            "Palmas hacia delante: flexiona los codos y despues estiralos controlando."
        )
    ),
    Ejercicio(
        "curl_martillo_polea", "Curl martillo en polea",
        Musculo.BICEPS, listOf(Musculo.ANTEBRAZO), Equipo.POLEA, false,
        "Agarre neutro y tension de abajo arriba. Va sobre todo al braquial, el musculo que empuja el biceps hacia fuera y hace que el brazo se vea mas grueso de lado.",
        Dificultad.PRINCIPIANTE,
        pasos = listOf(
            "Pon un mango individual en lo mas bajo de la maquina.",
            "Colocate de espaldas a la polea y adelanta un pie para no bailar.",
            "Agarre neutro, pulgar arriba y menique abajo.",
            "Flexiona el codo y estiralo despacio para completar la repeticion."
        )
    ),
    Ejercicio(
        "press_frances", "Press frances",
        Musculo.TRICEPS, emptyList(), Equipo.BARRA, false,
        "Los codos apuntan al techo y no se abren. Baja a la frente o justo por detras.",
        Dificultad.INTERMEDIO
    ),
    Ejercicio(
        "extension_polea", "Extension de triceps en polea",
        Musculo.TRICEPS, emptyList(), Equipo.POLEA, false,
        "Codos pegados al costado. Solo se mueve el antebrazo; el hombro es una bisagra fija.",
        Dificultad.PRINCIPIANTE
    ),
    Ejercicio(
        "extension_cuerda", "Extension con cuerda",
        Musculo.TRICEPS, emptyList(), Equipo.POLEA, false,
        "Abre la cuerda al final del recorrido para rematar la cabeza lateral.",
        Dificultad.PRINCIPIANTE
    ),
    Ejercicio(
        "fondos_banco", "Fondos en banco",
        Musculo.TRICEPS, listOf(Musculo.HOMBRO), Equipo.CORPORAL, false,
        "Facil de cargar poniendo peso en las piernas. Ojo al hombro: no bajes por debajo de los 90 grados.",
        Dificultad.PRINCIPIANTE
    ),
    Ejercicio(
        "press_cerrado", "Press de banca agarre cerrado",
        Musculo.TRICEPS, listOf(Musculo.PECHO, Musculo.HOMBRO), Equipo.BARRA, true,
        "Manos a la anchura de los hombros, ni mas juntas. Codos pegados al cuerpo al bajar.",
        Dificultad.INTERMEDIO
    ),
    Ejercicio(
        "patada_triceps", "Patada de triceps",
        Musculo.TRICEPS, emptyList(), Equipo.MANCUERNA, false,
        "Torso paralelo al suelo, brazo pegado. Poco peso y bloqueo completo arriba.",
        Dificultad.PRINCIPIANTE
    ),
    Ejercicio(
        "extension_sobre_cabeza", "Extension sobre la cabeza",
        Musculo.TRICEPS, emptyList(), Equipo.MANCUERNA, false,
        "Con el brazo por encima de la cabeza estiras la cabeza larga del triceps, que es la mas grande y la que casi nadie trabaja.",
        Dificultad.PRINCIPIANTE
    ),
    Ejercicio(
        "sentadilla", "Sentadilla con barra",
        Musculo.CUADRICEPS, listOf(Musculo.GLUTEO, Musculo.FEMORAL, Musculo.CORE), Equipo.BARRA, true,
        "Rompe con cadera y rodilla a la vez. Baja hasta que la cadera pase por debajo de la rodilla si tu movilidad lo permite, y sube con el pecho arriba.",
        Dificultad.INTERMEDIO
    ),
    Ejercicio(
        "sentadilla_frontal", "Sentadilla frontal",
        Musculo.CUADRICEPS, listOf(Musculo.CORE, Musculo.GLUTEO), Equipo.BARRA, true,
        "La barra delante obliga a llevar el torso vertical y carga mucho mas cuadriceps. Codos altos siempre.",
        Dificultad.AVANZADO
    ),
    Ejercicio(
        "prensa", "Prensa de piernas",
        Musculo.CUADRICEPS, listOf(Musculo.GLUTEO, Musculo.FEMORAL), Equipo.MAQUINA, true,
        "No bloquees las rodillas arriba y no dejes que la cadera se despegue abajo. Pies altos toca mas gluteo, pies bajos mas cuadriceps.",
        Dificultad.PRINCIPIANTE
    ),
    Ejercicio(
        "hack", "Sentadilla hack",
        Musculo.CUADRICEPS, listOf(Musculo.GLUTEO), Equipo.MAQUINA, true,
        "Trayectoria guiada: puedes ir al fallo con seguridad. Pies algo adelantados protegen la rodilla.",
        Dificultad.PRINCIPIANTE
    ),
    Ejercicio(
        "zancadas", "Zancadas",
        Musculo.CUADRICEPS, listOf(Musculo.GLUTEO, Musculo.FEMORAL), Equipo.MANCUERNA, true,
        "Paso largo carga gluteo, paso corto carga cuadriceps. La rodilla de atras baja hasta casi rozar.",
        Dificultad.INTERMEDIO
    ),
    Ejercicio(
        "bulgara", "Sentadilla bulgara",
        Musculo.CUADRICEPS, listOf(Musculo.GLUTEO), Equipo.MANCUERNA, true,
        "Pie de atras en el banco. Brutal a poco peso y arregla desequilibrios entre piernas mejor que ningun otro.",
        Dificultad.INTERMEDIO
    ),
    Ejercicio(
        "extension_cuadriceps", "Extension de cuadriceps",
        Musculo.CUADRICEPS, emptyList(), Equipo.MAQUINA, false,
        "Aislamiento puro. Aprieta un segundo arriba; no hace falta cargarla hasta el tope.",
        Dificultad.PRINCIPIANTE
    ),
    Ejercicio(
        "sentadilla_goblet", "Sentadilla goblet",
        Musculo.CUADRICEPS, listOf(Musculo.GLUTEO, Musculo.CORE), Equipo.MANCUERNA, true,
        "La mancuerna al pecho hace de contrapeso y corrige sola la tecnica. El mejor sitio para aprender a sentadillar.",
        Dificultad.PRINCIPIANTE
    ),
    Ejercicio(
        "step_up", "Subida al cajon",
        Musculo.CUADRICEPS, listOf(Musculo.GLUTEO), Equipo.MANCUERNA, true,
        "Sube empujando con el talon de la pierna de arriba, sin impulsarte con la de abajo.",
        Dificultad.PRINCIPIANTE
    ),
    Ejercicio(
        "sissy", "Sentadilla sissy",
        Musculo.CUADRICEPS, emptyList(), Equipo.CORPORAL, false,
        "Muy exigente para la rodilla, muy buena para el recto femoral. Solo si no tienes molestias previas.",
        Dificultad.AVANZADO
    ),
    Ejercicio(
        "curl_femoral_tumbado", "Curl femoral tumbado",
        Musculo.FEMORAL, listOf(Musculo.GEMELO), Equipo.MAQUINA, false,
        "Cadera pegada al banco. Si se levanta, sobra peso.",
        Dificultad.PRINCIPIANTE
    ),
    Ejercicio(
        "curl_femoral_sentado", "Curl femoral sentado",
        Musculo.FEMORAL, emptyList(), Equipo.MAQUINA, false,
        "Con la cadera flexionada el femoral se estira mas, y es la version que mas hipertrofia da.",
        Dificultad.PRINCIPIANTE
    ),
    Ejercicio(
        "peso_muerto_piernas_rectas", "Peso muerto a piernas rectas",
        Musculo.FEMORAL, listOf(Musculo.GLUTEO, Musculo.ESPALDA), Equipo.MANCUERNA, true,
        "Version con mancuernas del rumano. Mas facil de aprender y mas amable con la lumbar.",
        Dificultad.INTERMEDIO
    ),
    Ejercicio(
        "hip_thrust", "Hip thrust",
        Musculo.GLUTEO, listOf(Musculo.FEMORAL), Equipo.BARRA, true,
        "Espalda alta apoyada en el banco, barbilla metida. Empuja con los talones y aprieta el gluteo arriba, sin arquear la lumbar.",
        Dificultad.INTERMEDIO
    ),
    Ejercicio(
        "puente_gluteo", "Puente de gluteo",
        Musculo.GLUTEO, listOf(Musculo.FEMORAL), Equipo.CORPORAL, false,
        "La version de suelo del hip thrust. Sirve para calentar antes de sentadilla.",
        Dificultad.PRINCIPIANTE
    ),
    Ejercicio(
        "patada_gluteo_polea", "Patada de gluteo en polea",
        Musculo.GLUTEO, listOf(Musculo.FEMORAL), Equipo.POLEA, false,
        "Torso quieto y la pierna sale hacia atras y arriba. Sin arquear la espalda para ganar recorrido.",
        Dificultad.PRINCIPIANTE
    ),
    Ejercicio(
        "abduccion_maquina", "Abductores en maquina",
        Musculo.GLUTEO, emptyList(), Equipo.MAQUINA, false,
        "Gluteo medio, que es el que estabiliza la cadera al correr y al sentadillar. Inclinar el torso adelante lo pilla mejor.",
        Dificultad.PRINCIPIANTE
    ),
    Ejercicio(
        "nordic", "Curl nordico",
        Musculo.FEMORAL, emptyList(), Equipo.CORPORAL, false,
        "Baja lo mas despacio que puedas y ayudate con las manos al final. Lo mejor que hay contra las roturas de isquios.",
        Dificultad.AVANZADO
    ),
    Ejercicio(
        "swing_kettlebell", "Swing con kettlebell",
        Musculo.GLUTEO, listOf(Musculo.FEMORAL, Musculo.CORE), Equipo.KETTLEBELL, true,
        "Es una bisagra de cadera, no una sentadilla ni una elevacion de brazos. La pesa vuela por el impulso del gluteo.",
        Dificultad.AVANZADO
    ),
    Ejercicio(
        "gemelo_de_pie", "Elevacion de gemelos de pie",
        Musculo.GEMELO, emptyList(), Equipo.MAQUINA, false,
        "Recorrido completo: baja el talon hasta el estiramiento y sube hasta la punta. Pausa arriba y abajo.",
        Dificultad.PRINCIPIANTE
    ),
    Ejercicio(
        "gemelo_sentado", "Elevacion de gemelos sentado",
        Musculo.GEMELO, emptyList(), Equipo.MAQUINA, false,
        "Con la rodilla doblada trabaja el soleo, que es el que da grosor por debajo.",
        Dificultad.PRINCIPIANTE
    ),
    Ejercicio(
        "gemelo_prensa", "Gemelos en prensa",
        Musculo.GEMELO, emptyList(), Equipo.MAQUINA, false,
        "Punta de los pies en el borde de la plataforma y rodillas casi rectas.",
        Dificultad.PRINCIPIANTE
    ),
    Ejercicio(
        "plancha", "Plancha",
        Musculo.CORE, listOf(Musculo.HOMBRO), Equipo.CORPORAL, false,
        "Mete la cadera y aprieta gluteo: una plancha bien hecha cansa en treinta segundos. Si aguantas tres minutos, la estas haciendo mal.",
        Dificultad.PRINCIPIANTE
    ),
    Ejercicio(
        "rueda_abdominal", "Rueda abdominal",
        Musculo.CORE, listOf(Musculo.ESPALDA), Equipo.CORPORAL, false,
        "Estira sin dejar que la lumbar se hunda. Empieza de rodillas y con poco recorrido.",
        Dificultad.AVANZADO
    ),
    Ejercicio(
        "elevacion_piernas", "Elevacion de piernas colgado",
        Musculo.CORE, listOf(Musculo.ANTEBRAZO), Equipo.CORPORAL, false,
        "Rueda la pelvis al final, que es donde trabaja el abdominal; solo subir las piernas es flexor de cadera.",
        Dificultad.INTERMEDIO
    ),
    Ejercicio(
        "crunch_polea", "Crunch en polea",
        Musculo.CORE, emptyList(), Equipo.POLEA, false,
        "El unico abdominal al que puedes anadirle peso con facilidad. Redondea la espalda al bajar.",
        Dificultad.PRINCIPIANTE
    ),
    Ejercicio(
        "pallof", "Press Pallof",
        Musculo.CORE, listOf(Musculo.HOMBRO), Equipo.POLEA, false,
        "Antigiro: la polea intenta girarte y tu no la dejas. Poco espectacular y muy util.",
        Dificultad.PRINCIPIANTE
    ),
    Ejercicio(
        "paseo_granjero", "Paseo del granjero",
        Musculo.CORE, listOf(Musculo.ANTEBRAZO, Musculo.TRAPECIO), Equipo.MANCUERNA, true,
        "Camina con dos mancuernas pesadas, hombros atras y sin inclinarte. Agarre, core y caracter en un solo ejercicio.",
        Dificultad.INTERMEDIO
    ),
    Ejercicio(
        "bicho_muerto", "Bicho muerto",
        Musculo.CORE, emptyList(), Equipo.CORPORAL, false,
        "Lumbar pegada al suelo todo el rato. En cuanto se despega, se acabo la serie.",
        Dificultad.PRINCIPIANTE
    ),
    Ejercicio(
        "curl_muneca", "Curl de muneca",
        Musculo.ANTEBRAZO, emptyList(), Equipo.BARRA, false,
        "Antebrazos apoyados en el banco. Recorrido corto y repeticiones altas.",
        Dificultad.PRINCIPIANTE
    ),
    Ejercicio(
        "colgarse_barra", "Colgarse de la barra",
        Musculo.ANTEBRAZO, listOf(Musculo.ESPALDA), Equipo.CORPORAL, false,
        "Aguanta lo que puedas. Mejora el agarre y descomprime la columna, que despues de sentadillar se agradece.",
        Dificultad.PRINCIPIANTE
    )
)

/** Busca por id. Devuelve null solo si alguien escribio mal un id a mano. */
fun ejercicioDe(id: String): Ejercicio? = CATALOGO.firstOrNull { it.id == id }

/** El catalogo indexado, que es lo que usan las pantallas para no recorrerlo entero. */
val CATALOGO_POR_ID: Map<String, Ejercicio> = CATALOGO.associateBy { it.id }
