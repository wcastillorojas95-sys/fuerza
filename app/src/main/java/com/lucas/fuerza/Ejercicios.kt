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

enum class Equipo(val etiqueta: String) {
    BARRA("Barra"),
    MANCUERNA("Mancuerna"),
    POLEA("Polea"),
    MAQUINA("Maquina"),
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
 */
data class Ejercicio(
    val id: String,
    val nombre: String,
    val musculo: Musculo,
    val secundarios: List<Musculo>,
    val equipo: Equipo,
    val compuesto: Boolean,
    val claves: String
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
        "Escapulas metidas y pegadas al banco, pies clavados en el suelo. La barra baja al esternon, no al cuello, y sube en diagonal ligera hacia la cara."
    ),
    Ejercicio(
        "press_banca_inclinado", "Press inclinado con barra",
        Musculo.PECHO, listOf(Musculo.HOMBRO, Musculo.TRICEPS), Equipo.BARRA, true,
        "Banco entre 30 y 45 grados; mas inclinacion y el trabajo se va al hombro. Baja a la clavicula."
    ),
    Ejercicio(
        "press_banca_declinado", "Press declinado con barra",
        Musculo.PECHO, listOf(Musculo.TRICEPS), Equipo.BARRA, true,
        "Pega mas al pectoral bajo. Baja al final de las costillas y no bloquees del todo arriba."
    ),
    Ejercicio(
        "press_mancuernas", "Press de banca con mancuernas",
        Musculo.PECHO, listOf(Musculo.TRICEPS, Musculo.HOMBRO), Equipo.MANCUERNA, true,
        "Mas recorrido que con barra y cada lado carga lo suyo. Abajo, los codos no pasan mucho de la linea del torso."
    ),
    Ejercicio(
        "press_incl_mancuernas", "Press inclinado con mancuernas",
        Musculo.PECHO, listOf(Musculo.HOMBRO, Musculo.TRICEPS), Equipo.MANCUERNA, true,
        "El favorito para pectoral superior. Junta las mancuernas arriba sin llegar a chocarlas."
    ),
    Ejercicio(
        "aperturas_mancuerna", "Aperturas con mancuernas",
        Musculo.PECHO, emptyList(), Equipo.MANCUERNA, false,
        "Codos semiflexionados y fijos todo el rato: si se abren y cierran, es un press. Abre hasta notar el estiramiento, no mas."
    ),
    Ejercicio(
        "aperturas_polea", "Cruce de poleas",
        Musculo.PECHO, emptyList(), Equipo.POLEA, false,
        "La polea mantiene tension tambien arriba, que es donde la mancuerna la pierde. Cruza un poco las manos al final."
    ),
    Ejercicio(
        "press_maquina_pecho", "Press de pecho en maquina",
        Musculo.PECHO, listOf(Musculo.TRICEPS), Equipo.MAQUINA, true,
        "El sitio para llevar una serie al fallo sin necesitar quien te ayude. Ajusta el asiento para que las manos queden a la altura del pecho."
    ),
    Ejercicio(
        "peck_deck", "Contractor de pecho (peck deck)",
        Musculo.PECHO, emptyList(), Equipo.MAQUINA, false,
        "Aislamiento puro. Aprieta un segundo al juntar y vuelve despacio."
    ),
    Ejercicio(
        "fondos_pecho", "Fondos en paralelas",
        Musculo.PECHO, listOf(Musculo.TRICEPS, Musculo.HOMBRO), Equipo.CORPORAL, true,
        "Inclinate hacia delante para cargar pectoral; vertical carga triceps. Baja hasta que el hombro quede a la altura del codo y ni un dedo mas."
    ),
    Ejercicio(
        "flexiones", "Flexiones",
        Musculo.PECHO, listOf(Musculo.TRICEPS, Musculo.CORE), Equipo.CORPORAL, true,
        "El cuerpo es una tabla de la cabeza a los talones. Codos a unos 45 grados del torso, no abiertos en cruz."
    ),
    Ejercicio(
        "pullover", "Pullover con mancuerna",
        Musculo.PECHO, listOf(Musculo.ESPALDA, Musculo.CORE), Equipo.MANCUERNA, false,
        "Tumbado, la mancuerna baja por detras de la cabeza con los codos casi rectos. Abre caja toracica y toca dorsal a la vez."
    ),
    Ejercicio(
        "dominadas", "Dominadas",
        Musculo.ESPALDA, listOf(Musculo.BICEPS, Musculo.ANTEBRAZO), Equipo.CORPORAL, true,
        "Empieza el tiron bajando los hombros, no doblando el codo. Sube hasta pasar la barbilla y baja controlando, sin caer en peso muerto."
    ),
    Ejercicio(
        "dominadas_supinas", "Dominadas supinas",
        Musculo.ESPALDA, listOf(Musculo.BICEPS), Equipo.CORPORAL, true,
        "Palmas hacia ti: entra mucho mas biceps y suele salir alguna repeticion mas que en pronacion."
    ),
    Ejercicio(
        "jalon_pecho", "Jalon al pecho",
        Musculo.ESPALDA, listOf(Musculo.BICEPS), Equipo.POLEA, true,
        "La barra baja a la clavicula con el pecho sacado. Nada de tirar por detras de la nuca: no aporta y castiga el hombro."
    ),
    Ejercicio(
        "jalon_agarre_neutro", "Jalon con agarre neutro",
        Musculo.ESPALDA, listOf(Musculo.BICEPS), Equipo.POLEA, true,
        "Manos enfrentadas. Es el agarre mas amable con el hombro y el que mas dorsal bajo pilla."
    ),
    Ejercicio(
        "remo_barra", "Remo con barra",
        Musculo.ESPALDA, listOf(Musculo.BICEPS, Musculo.FEMORAL), Equipo.BARRA, true,
        "Torso a unos 45 grados y espalda plana. La barra va al ombligo, no al pecho, y el tiron sale de los codos."
    ),
    Ejercicio(
        "remo_pendlay", "Remo Pendlay",
        Musculo.ESPALDA, listOf(Musculo.TRAPECIO, Musculo.BICEPS), Equipo.BARRA, true,
        "Torso paralelo al suelo y la barra se apoya en el suelo en cada repeticion. Explosivo al subir, estricto en la posicion."
    ),
    Ejercicio(
        "remo_mancuerna", "Remo con mancuerna a una mano",
        Musculo.ESPALDA, listOf(Musculo.BICEPS), Equipo.MANCUERNA, true,
        "Una rodilla en el banco, espalda plana. Lleva el codo hacia la cadera y deja que la escapula viaje al final."
    ),
    Ejercicio(
        "remo_sentado_polea", "Remo sentado en polea",
        Musculo.ESPALDA, listOf(Musculo.BICEPS), Equipo.POLEA, true,
        "Rodillas algo flexionadas y torso quieto: si te balanceas hacia atras, sobra peso. Saca pecho al recoger."
    ),
    Ejercicio(
        "remo_maquina", "Remo en maquina",
        Musculo.ESPALDA, listOf(Musculo.BICEPS), Equipo.MAQUINA, true,
        "Pecho apoyado, que quita a la lumbar del medio. Ideal para acumular volumen sin castigar la espalda baja."
    ),
    Ejercicio(
        "remo_t", "Remo en T",
        Musculo.ESPALDA, listOf(Musculo.TRAPECIO, Musculo.BICEPS), Equipo.BARRA, true,
        "Buen equilibrio entre dorsal y espalda media. Agarre neutro y recorrido completo."
    ),
    Ejercicio(
        "peso_muerto", "Peso muerto",
        Musculo.ESPALDA, listOf(Musculo.FEMORAL, Musculo.GLUTEO, Musculo.TRAPECIO), Equipo.BARRA, true,
        "La barra roza la espinilla todo el camino. Empuja el suelo con los pies en vez de tirar con la espalda; el bloqueo es de cadera, no de lumbar."
    ),
    Ejercicio(
        "peso_muerto_rumano", "Peso muerto rumano",
        Musculo.FEMORAL, listOf(Musculo.GLUTEO, Musculo.ESPALDA), Equipo.BARRA, true,
        "Rodillas casi rectas y la cadera va hacia atras. Baja hasta notar el femoral, no hasta tocar el suelo."
    ),
    Ejercicio(
        "pullover_polea", "Pullover en polea alta",
        Musculo.ESPALDA, emptyList(), Equipo.POLEA, false,
        "Codos rectos, la barra baja en arco hasta los muslos. Es de lo poco que aisla dorsal sin meter biceps."
    ),
    Ejercicio(
        "face_pull", "Face pull",
        Musculo.HOMBRO, listOf(Musculo.TRAPECIO, Musculo.ESPALDA), Equipo.POLEA, false,
        "Polea a la altura de la cara, tira de la cuerda separando las manos hacia las orejas. El mejor seguro barato para hombros que hacen mucho press."
    ),
    Ejercicio(
        "encogimientos", "Encogimientos de hombros",
        Musculo.TRAPECIO, listOf(Musculo.ANTEBRAZO), Equipo.MANCUERNA, false,
        "Sube recto, sin rodar el hombro hacia atras. Aguanta arriba un segundo."
    ),
    Ejercicio(
        "hiperextensiones", "Hiperextensiones",
        Musculo.ESPALDA, listOf(Musculo.GLUTEO, Musculo.FEMORAL), Equipo.CORPORAL, false,
        "Sube hasta la linea del cuerpo y para: pasarse de ahi solo comprime la lumbar."
    ),
    Ejercicio(
        "buenos_dias", "Buenos dias",
        Musculo.FEMORAL, listOf(Musculo.ESPALDA, Musculo.GLUTEO), Equipo.BARRA, true,
        "Poco peso y mucha tecnica. La barra alta en la espalda, cadera atras y espalda plana como una mesa."
    ),
    Ejercicio(
        "press_militar", "Press militar con barra",
        Musculo.HOMBRO, listOf(Musculo.TRICEPS, Musculo.CORE), Equipo.BARRA, true,
        "De pie, gluteo y abdomen apretados para no arquear la lumbar. La cabeza se retira un poco para dejar pasar la barra."
    ),
    Ejercicio(
        "press_hombro_mancuernas", "Press de hombro con mancuernas",
        Musculo.HOMBRO, listOf(Musculo.TRICEPS), Equipo.MANCUERNA, true,
        "Sentado con respaldo si te cuesta mantener la lumbar. Codos ligeramente adelantados, no en cruz."
    ),
    Ejercicio(
        "press_arnold", "Press Arnold",
        Musculo.HOMBRO, listOf(Musculo.TRICEPS), Equipo.MANCUERNA, true,
        "Empieza con palmas hacia ti y gira al subir. Pilla deltoides anterior y medio en el mismo viaje."
    ),
    Ejercicio(
        "elevaciones_laterales", "Elevaciones laterales",
        Musculo.HOMBRO, emptyList(), Equipo.MANCUERNA, false,
        "El ejercicio que ensancha. Sube hasta la horizontal guiando con el codo, no con la mano, y baja en tres tiempos."
    ),
    Ejercicio(
        "elevaciones_lateral_polea", "Elevacion lateral en polea",
        Musculo.HOMBRO, emptyList(), Equipo.POLEA, false,
        "La polea da tension desde el primer grado, que es justo donde la mancuerna no da nada."
    ),
    Ejercicio(
        "elevaciones_frontales", "Elevaciones frontales",
        Musculo.HOMBRO, emptyList(), Equipo.MANCUERNA, false,
        "Suele sobrar si haces press: el deltoides anterior ya trabaja de mas. Uselo con moderacion."
    ),
    Ejercicio(
        "pajaros", "Pajaros (deltoides posterior)",
        Musculo.HOMBRO, listOf(Musculo.ESPALDA), Equipo.MANCUERNA, false,
        "Torso casi paralelo al suelo. Abre con los codos, no con las manos, y no cojas peso: aqui nadie es fuerte."
    ),
    Ejercicio(
        "press_hombro_maquina", "Press de hombro en maquina",
        Musculo.HOMBRO, listOf(Musculo.TRICEPS), Equipo.MAQUINA, true,
        "Trayectoria fija, ideal para las ultimas series cuando el equilibrio ya falla."
    ),
    Ejercicio(
        "remo_menton", "Remo al menton",
        Musculo.HOMBRO, listOf(Musculo.TRAPECIO), Equipo.BARRA, true,
        "Agarre ancho y sube solo hasta el pecho. Agarre estrecho y subir hasta la barbilla pinza el hombro."
    ),
    Ejercicio(
        "curl_barra", "Curl con barra",
        Musculo.BICEPS, listOf(Musculo.ANTEBRAZO), Equipo.BARRA, false,
        "Codos pegados al costado y quietos. Si la cadera acompana el movimiento, es que hay demasiado disco."
    ),
    Ejercicio(
        "curl_mancuernas", "Curl con mancuernas",
        Musculo.BICEPS, listOf(Musculo.ANTEBRAZO), Equipo.MANCUERNA, false,
        "Gira la muneca hacia fuera al subir: el biceps tambien supina, y asi hace su trabajo completo."
    ),
    Ejercicio(
        "curl_martillo", "Curl martillo",
        Musculo.BICEPS, listOf(Musculo.ANTEBRAZO), Equipo.MANCUERNA, false,
        "Palmas enfrentadas. Es el que engorda el braquial, que es el musculo que empuja el biceps hacia arriba."
    ),
    Ejercicio(
        "curl_predicador", "Curl en banco predicador",
        Musculo.BICEPS, emptyList(), Equipo.BARRA, false,
        "El banco impide hacer trampa con el hombro. No estires del todo abajo si tienes el codo delicado."
    ),
    Ejercicio(
        "curl_inclinado", "Curl inclinado en banco",
        Musculo.BICEPS, emptyList(), Equipo.MANCUERNA, false,
        "El brazo cae por detras del cuerpo y estira el biceps al maximo. Duele bien."
    ),
    Ejercicio(
        "curl_polea", "Curl en polea baja",
        Musculo.BICEPS, emptyList(), Equipo.POLEA, false,
        "Tension constante de abajo arriba. Va muy bien como ultimo ejercicio del dia de tiron."
    ),
    Ejercicio(
        "curl_concentrado", "Curl concentrado",
        Musculo.BICEPS, emptyList(), Equipo.MANCUERNA, false,
        "Codo apoyado en el muslo. Nada de peso, todo de contraccion."
    ),
    Ejercicio(
        "press_frances", "Press frances",
        Musculo.TRICEPS, emptyList(), Equipo.BARRA, false,
        "Los codos apuntan al techo y no se abren. Baja a la frente o justo por detras."
    ),
    Ejercicio(
        "extension_polea", "Extension de triceps en polea",
        Musculo.TRICEPS, emptyList(), Equipo.POLEA, false,
        "Codos pegados al costado. Solo se mueve el antebrazo; el hombro es una bisagra fija."
    ),
    Ejercicio(
        "extension_cuerda", "Extension con cuerda",
        Musculo.TRICEPS, emptyList(), Equipo.POLEA, false,
        "Abre la cuerda al final del recorrido para rematar la cabeza lateral."
    ),
    Ejercicio(
        "fondos_banco", "Fondos en banco",
        Musculo.TRICEPS, listOf(Musculo.HOMBRO), Equipo.CORPORAL, false,
        "Facil de cargar poniendo peso en las piernas. Ojo al hombro: no bajes por debajo de los 90 grados."
    ),
    Ejercicio(
        "press_cerrado", "Press de banca agarre cerrado",
        Musculo.TRICEPS, listOf(Musculo.PECHO, Musculo.HOMBRO), Equipo.BARRA, true,
        "Manos a la anchura de los hombros, ni mas juntas. Codos pegados al cuerpo al bajar."
    ),
    Ejercicio(
        "patada_triceps", "Patada de triceps",
        Musculo.TRICEPS, emptyList(), Equipo.MANCUERNA, false,
        "Torso paralelo al suelo, brazo pegado. Poco peso y bloqueo completo arriba."
    ),
    Ejercicio(
        "extension_sobre_cabeza", "Extension sobre la cabeza",
        Musculo.TRICEPS, emptyList(), Equipo.MANCUERNA, false,
        "Con el brazo por encima de la cabeza estiras la cabeza larga del triceps, que es la mas grande y la que casi nadie trabaja."
    ),
    Ejercicio(
        "sentadilla", "Sentadilla con barra",
        Musculo.CUADRICEPS, listOf(Musculo.GLUTEO, Musculo.FEMORAL, Musculo.CORE), Equipo.BARRA, true,
        "Rompe con cadera y rodilla a la vez. Baja hasta que la cadera pase por debajo de la rodilla si tu movilidad lo permite, y sube con el pecho arriba."
    ),
    Ejercicio(
        "sentadilla_frontal", "Sentadilla frontal",
        Musculo.CUADRICEPS, listOf(Musculo.CORE, Musculo.GLUTEO), Equipo.BARRA, true,
        "La barra delante obliga a llevar el torso vertical y carga mucho mas cuadriceps. Codos altos siempre."
    ),
    Ejercicio(
        "prensa", "Prensa de piernas",
        Musculo.CUADRICEPS, listOf(Musculo.GLUTEO, Musculo.FEMORAL), Equipo.MAQUINA, true,
        "No bloquees las rodillas arriba y no dejes que la cadera se despegue abajo. Pies altos toca mas gluteo, pies bajos mas cuadriceps."
    ),
    Ejercicio(
        "hack", "Sentadilla hack",
        Musculo.CUADRICEPS, listOf(Musculo.GLUTEO), Equipo.MAQUINA, true,
        "Trayectoria guiada: puedes ir al fallo con seguridad. Pies algo adelantados protegen la rodilla."
    ),
    Ejercicio(
        "zancadas", "Zancadas",
        Musculo.CUADRICEPS, listOf(Musculo.GLUTEO, Musculo.FEMORAL), Equipo.MANCUERNA, true,
        "Paso largo carga gluteo, paso corto carga cuadriceps. La rodilla de atras baja hasta casi rozar."
    ),
    Ejercicio(
        "bulgara", "Sentadilla bulgara",
        Musculo.CUADRICEPS, listOf(Musculo.GLUTEO), Equipo.MANCUERNA, true,
        "Pie de atras en el banco. Brutal a poco peso y arregla desequilibrios entre piernas mejor que ningun otro."
    ),
    Ejercicio(
        "extension_cuadriceps", "Extension de cuadriceps",
        Musculo.CUADRICEPS, emptyList(), Equipo.MAQUINA, false,
        "Aislamiento puro. Aprieta un segundo arriba; no hace falta cargarla hasta el tope."
    ),
    Ejercicio(
        "sentadilla_goblet", "Sentadilla goblet",
        Musculo.CUADRICEPS, listOf(Musculo.GLUTEO, Musculo.CORE), Equipo.MANCUERNA, true,
        "La mancuerna al pecho hace de contrapeso y corrige sola la tecnica. El mejor sitio para aprender a sentadillar."
    ),
    Ejercicio(
        "step_up", "Subida al cajon",
        Musculo.CUADRICEPS, listOf(Musculo.GLUTEO), Equipo.MANCUERNA, true,
        "Sube empujando con el talon de la pierna de arriba, sin impulsarte con la de abajo."
    ),
    Ejercicio(
        "sissy", "Sentadilla sissy",
        Musculo.CUADRICEPS, emptyList(), Equipo.CORPORAL, false,
        "Muy exigente para la rodilla, muy buena para el recto femoral. Solo si no tienes molestias previas."
    ),
    Ejercicio(
        "curl_femoral_tumbado", "Curl femoral tumbado",
        Musculo.FEMORAL, listOf(Musculo.GEMELO), Equipo.MAQUINA, false,
        "Cadera pegada al banco. Si se levanta, sobra peso."
    ),
    Ejercicio(
        "curl_femoral_sentado", "Curl femoral sentado",
        Musculo.FEMORAL, emptyList(), Equipo.MAQUINA, false,
        "Con la cadera flexionada el femoral se estira mas, y es la version que mas hipertrofia da."
    ),
    Ejercicio(
        "peso_muerto_piernas_rectas", "Peso muerto a piernas rectas",
        Musculo.FEMORAL, listOf(Musculo.GLUTEO, Musculo.ESPALDA), Equipo.MANCUERNA, true,
        "Version con mancuernas del rumano. Mas facil de aprender y mas amable con la lumbar."
    ),
    Ejercicio(
        "hip_thrust", "Hip thrust",
        Musculo.GLUTEO, listOf(Musculo.FEMORAL), Equipo.BARRA, true,
        "Espalda alta apoyada en el banco, barbilla metida. Empuja con los talones y aprieta el gluteo arriba, sin arquear la lumbar."
    ),
    Ejercicio(
        "puente_gluteo", "Puente de gluteo",
        Musculo.GLUTEO, listOf(Musculo.FEMORAL), Equipo.CORPORAL, false,
        "La version de suelo del hip thrust. Sirve para calentar antes de sentadilla."
    ),
    Ejercicio(
        "patada_gluteo_polea", "Patada de gluteo en polea",
        Musculo.GLUTEO, listOf(Musculo.FEMORAL), Equipo.POLEA, false,
        "Torso quieto y la pierna sale hacia atras y arriba. Sin arquear la espalda para ganar recorrido."
    ),
    Ejercicio(
        "abduccion_maquina", "Abductores en maquina",
        Musculo.GLUTEO, emptyList(), Equipo.MAQUINA, false,
        "Gluteo medio, que es el que estabiliza la cadera al correr y al sentadillar. Inclinar el torso adelante lo pilla mejor."
    ),
    Ejercicio(
        "nordic", "Curl nordico",
        Musculo.FEMORAL, emptyList(), Equipo.CORPORAL, false,
        "Baja lo mas despacio que puedas y ayudate con las manos al final. Lo mejor que hay contra las roturas de isquios."
    ),
    Ejercicio(
        "swing_kettlebell", "Swing con kettlebell",
        Musculo.GLUTEO, listOf(Musculo.FEMORAL, Musculo.CORE), Equipo.KETTLEBELL, true,
        "Es una bisagra de cadera, no una sentadilla ni una elevacion de brazos. La pesa vuela por el impulso del gluteo."
    ),
    Ejercicio(
        "gemelo_de_pie", "Elevacion de gemelos de pie",
        Musculo.GEMELO, emptyList(), Equipo.MAQUINA, false,
        "Recorrido completo: baja el talon hasta el estiramiento y sube hasta la punta. Pausa arriba y abajo."
    ),
    Ejercicio(
        "gemelo_sentado", "Elevacion de gemelos sentado",
        Musculo.GEMELO, emptyList(), Equipo.MAQUINA, false,
        "Con la rodilla doblada trabaja el soleo, que es el que da grosor por debajo."
    ),
    Ejercicio(
        "gemelo_prensa", "Gemelos en prensa",
        Musculo.GEMELO, emptyList(), Equipo.MAQUINA, false,
        "Punta de los pies en el borde de la plataforma y rodillas casi rectas."
    ),
    Ejercicio(
        "plancha", "Plancha",
        Musculo.CORE, listOf(Musculo.HOMBRO), Equipo.CORPORAL, false,
        "Mete la cadera y aprieta gluteo: una plancha bien hecha cansa en treinta segundos. Si aguantas tres minutos, la estas haciendo mal."
    ),
    Ejercicio(
        "rueda_abdominal", "Rueda abdominal",
        Musculo.CORE, listOf(Musculo.ESPALDA), Equipo.CORPORAL, false,
        "Estira sin dejar que la lumbar se hunda. Empieza de rodillas y con poco recorrido."
    ),
    Ejercicio(
        "elevacion_piernas", "Elevacion de piernas colgado",
        Musculo.CORE, listOf(Musculo.ANTEBRAZO), Equipo.CORPORAL, false,
        "Rueda la pelvis al final, que es donde trabaja el abdominal; solo subir las piernas es flexor de cadera."
    ),
    Ejercicio(
        "crunch_polea", "Crunch en polea",
        Musculo.CORE, emptyList(), Equipo.POLEA, false,
        "El unico abdominal al que puedes anadirle peso con facilidad. Redondea la espalda al bajar."
    ),
    Ejercicio(
        "pallof", "Press Pallof",
        Musculo.CORE, listOf(Musculo.HOMBRO), Equipo.POLEA, false,
        "Antigiro: la polea intenta girarte y tu no la dejas. Poco espectacular y muy util."
    ),
    Ejercicio(
        "paseo_granjero", "Paseo del granjero",
        Musculo.CORE, listOf(Musculo.ANTEBRAZO, Musculo.TRAPECIO), Equipo.MANCUERNA, true,
        "Camina con dos mancuernas pesadas, hombros atras y sin inclinarte. Agarre, core y caracter en un solo ejercicio."
    ),
    Ejercicio(
        "bicho_muerto", "Bicho muerto",
        Musculo.CORE, emptyList(), Equipo.CORPORAL, false,
        "Lumbar pegada al suelo todo el rato. En cuanto se despega, se acabo la serie."
    ),
    Ejercicio(
        "curl_muneca", "Curl de muneca",
        Musculo.ANTEBRAZO, emptyList(), Equipo.BARRA, false,
        "Antebrazos apoyados en el banco. Recorrido corto y repeticiones altas."
    ),
    Ejercicio(
        "colgarse_barra", "Colgarse de la barra",
        Musculo.ANTEBRAZO, listOf(Musculo.ESPALDA), Equipo.CORPORAL, false,
        "Aguanta lo que puedas. Mejora el agarre y descomprime la columna, que despues de sentadillar se agradece."
    )
)

/** Busca por id. Devuelve null solo si alguien escribio mal un id a mano. */
fun ejercicioDe(id: String): Ejercicio? = CATALOGO.firstOrNull { it.id == id }

/** El catalogo indexado, que es lo que usan las pantallas para no recorrerlo entero. */
val CATALOGO_POR_ID: Map<String, Ejercicio> = CATALOGO.associateBy { it.id }
