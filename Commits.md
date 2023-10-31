# GameProject_MistBorn
Proyecto final de la materia "programacion sobre redes". 

*Los objetivos actuales estaran escritos en el codigo, a medida que los vayamos haciendo los borraremos y pondremos otros.*

*Ejecutar este juego: 
- Ejecutar el archivo MistBorn.bat en la carpeta raiz.
- Por consola, en la carpeta raiz del juego(cd "carpeta"), hay que ejecutar este comando: gradlew desktop:run. * 

## Commits (del mas actual al mas antiguo)

### "textura de la moneda y GestorMonedas" - Fecha 31/10

- Añadimos la textura de las monedas.
- Para dibujar las mismas tuvimos que modificar la clase Basura y ahora se llama GestorMonedas porque nos dimos cuenta de que se puede utilizar para dibujar las monedas en PantallaPvP.
- El metodo añadirBasura() de la clase GestorMoneda tambien se utiliza en Colision porque ahi detecta mejor las monedas que se borran.

### "renovando PantallaCarga y PantallaMenu con Scene2D" - Fecha 31/10

- Cambiamos las pantallas mencionadas para optimizar codigo con Scene2D.
- Borramos las anteriores pantallas.
- Modificamos el diseño de los botones del menu. Lo demas quedo bastante parecido. 

### "poniendo cursor personalizado" - Fecha 30/10

- Creamos los cursores para el mouse y para la colision del Mouse con los bodies de box2D.
- Los implementamos en ColisionMouse y PantallaPvP.
- Archivos XCF y assets estan subidos.

### "terminamos los poderes, barras de HUD adaptadas y mas cosas" - Fecha 29/10

- Terminamos lo que vendrian a ser todos los poderes del juego, hicimos el peltre y hicimos cambio de opcion con la X en el poder de Acero (el concepto esta en el PNG que lo explica).
- Creamos la clase Basura para borrar mas facilmente las monedas inutiles, porque antes teniamos problemas al estar todo en una clase (Disparo).
- Creamos 2 interfaces mas para realizar eventos, una para reducirPoder() en el Hud, cuando se esta quemando uno y la otra interfaz para decirle al hud la duracion del poder del Peltre.
- Creamos un enumerador OpcionAcero para switchear entre DISPARO Y EMPUJE cuando toca la tecla X. Tambien nos sirve en la clase Personaje para dibujar la ColisionMouse si es EMPUJE.
- Hemos hecho cambios en los poderes tambien para todo el tema del gasto de energia. 

### "poder Hierro hecho,ColisionMouse,muchas cosas" - Fecha 29/10

- Terminamos de hacer el poder Hierro, anda como lo esperado.
- Para hacer el Hierro creamos la clase ColisionMouse para detectar a traves de un rayo los bodies metalicos del mundo.
- En base a eso creamos la clase ObjetoMetalico, para crear estos objetos de metal.
- Tuvimos que modificar bastantes cosas como la interfaz Disparable, la clase Disparo un poco, porque nos convenia para hacer el poder de Hierro, creemos que lo supimos adaptar.
- Tambien hemos hecho modificaciones en la clase Poder, Personaje,PantallaPvP, entre otras.

### "adaptando Disparo a Acero, ESC para PantallaSeleccion y evento restarMoneda()" - Fecha 25/10

- Adaptamos la clase Disparo al poder de Acero, ya ahora cuando selecciona el poder acero puede disparar joya.
- Para el item anterior creamos la interfaz Disparable, esta interfaz la tiene solo la clase Acero. 
- Hicimos el evento de restarMoneda() que se utiliza en la clase HUD y Personaje.
- Creamos el label en Hud que muestra el contador de monedas, esto ya esta adaptado junto al disparo, osea que cuando gasta una moneda, esta se resta a traves del evento.
- Creamos el enum TipoPoder para la creacion del label del item anterior.
 
### "haciendo poderes" - Fecha 24/10

- Adaptamos la clase Poder, junto con sus hijas, a la clase Personaje, cuando toque 1,2 o R se activan los poderes. (falta el sistema de reduccion de pts en el hud).
- Creamos el enum TipoPersonaje justamente para el item anterior, para saber si el pj es Nacido de la bruma o brumoso.
- Adaptamos (y falta un poco) la clase Disparo a el Poder Acero.

### "solucionando error y reducirVida()" - Fecha 23/10

- Solucionamos un error que no nos dejaba elegir al pj Vin porque habia un problema en la clase de Listeners.
- Hicimos la colision de moneda-pj la cual reduce la vida del Hud cuando colisionan mientras el pj esta disparando.
- Con respecto al item anterior, no esta bien hecho ya que hay un hud global el cual no se diferencian la vida de los 2 pjs.

### "terminamos Disparo,y viendo eventos Hud" - Fecha 21/10

- Terminamos la clase de Disparo, todo funciona como lo esperado.
- Respecto al item anterior,modificamos la clase Colision para las paredes, clase Moneda y enum UserData.
- Viendo como hacer los eventos de reducirVida() y reducirPoderes() en la clase HUD y Personaje.

### "Disparo,Colision y arreglo minimo PantallaSeleccion" - Fecha 19/10

- Logramos hacer que el disparo de la moneda fuera como quisimos. Hablando de la logica.
- Tocamos la clase Colision para detectar las colisiones de las monedas con el piso y las plataformas. Si las monedas estan inactivas, se eliminan del mundo.
- Creamos la clase Moneda para que nos sirva tanto para crear el body (con su textura) cuando se dispara la misma y tambien para futuro guardar instancias de esa clase en el inventario del jugador en vez de Body.
- Error en la PantallaSeleccion. Cuando tocabas ENTER para seleccionar el pj y despues seleccionabas otro (fade activado),en el juego se te ponia el ultimo que seleccionaste en vez de que sea el que tocaste ENTER.

### "cambiando Entradas y lidiando todavia con Disparo" - Fecha 18/10

- Cambiamos las Entradas para los 2 botones principales del mouse, tuvimos que cambiar los booleanos de Entradas de las Pantallas.
- Siguiendo con Disparo hasta ahora, creamos 3 booleanos (apuntando,disparando,balaDisparada), en base a eso se hacen los calculos con Box2D, en proceso.

### "terminamos reflection y siguiendo disparo" - Fecha 17/10

- Ahora cuando seleccionamos el pj en la PantallaSeleccion se nos creara en la PantallaPvP a traves de Reflection
- Estamos viendo como hacer la clase Disparo.

### "solucionando direccion del disparo" - Fecha 17/10

- En la clase Disparo solucionamos la direccion del disparo respecto al cursor.

### "implementando barra de peltre"  - Fecha 16/10

- Diseñamos el marco del poder peltre.
- Cargado en Archivos XCF y assets/Hud

### "poderes en Hud,evento" - Fecha 12/10

- Creamos la clase Listeners para el manejo de eventos.
- Creamos la interfaz EventoCrearBarra, utilizada en Listeners para decirle al Hud que cree cierta barra de poder en base al personaje elegido.
- Implementamos las barras de los poderes en Recursos y en las clases de Poder.
- Modificando Disparo para resolver problema (todavia no resuelto).

### "implementando barra de acero y hierro" - Fecha 9/10

- Diseñamos las dos barras de metales, como era la de la vida.

### "haciendo Disparo y creando clases" - Fecha 8/10

- Codeamos un poco como funciona los disparos.
- Creamos las clases de los 3 poderes del videojuego, aunque todavia no lo empezamos.
- Adjuntamos la carpeta del Skin Composer.

### "pensando mecanica del pvp" - Fecha 7/10

- Adjuntamos un PNG que muestra el objetivo final a llegar con el tema del PvP.
- Creamos una clase Disparo para ir probando como disparar las monedas, aunque no estamos muy seguros todavia de como introducirnos en este tema. 

### "agregando sonidos y fondo PantallaSeleccion" - Fecha 26/09

- Creamos e implementamos un background en la PantallaSeleccion.
- Añadimos SFX para cuando cambia de seleccion y toca ENTER.
- Adjuntamos el archivo XCF del background.

### "ultimo commit antes de la entrega TP 2" - Fecha 24/09

- Terminamos el diseño de los widgets de Scene2DUI, utilizando Skin Composer. (nos llegamos a hacer el fondo). (PantallaSeleccion).
- Pusimos el boton de la cabeza de Ham. Y tambien la informacion (texto,imagen,nombre) se adecuan a la opcion que selecciona el usuario. (PantallaSeleccion).
- Respecto a añadir fuentes, creamos la clase SkinFreeTypeLoader y el enum Fuente, todo esto para que sea mas optimo para las Pantallas.
- Creamos las clases de los Pj faltantes, aunque faltan las texturas que le corresponden a cada uno, pero para ya tenerlo.
- Utilizamos Actions (clase de Scene2DUI) para hacer efectos (fadeIn,fadeOut).
- La cancion del menu ahora estara activa hasta la PantallaSeleccion.
- Hicimos un chequeo final de la mayoria de las Clases.

### "definiendo seleccion y resoluciones" - Fecha 23/09

- Terminamos de resolver el tema que teniamos con las Tables en la PantallaSeleccion, quedo bien.
- Ajustamos el fondo de la PantallaPvP para las diferentes resoluciones.
- Para el punto anterior,tuvimos que modificar la clase Imagen.
- Añadimos un archivo.xcf que faltaba.

### "Agregando al nuevo personaje, Ham" - Fecha 23/09

- Hicimos e implementamos las nuevas texturas que corresponden a el segundo personaje del juego.
- Animaciones: correr, salto y quieto.

### "lidiando con Scene2DUI Y locale" - Fecha 22/09

- Nos pasamos viendo como seguir haciendo la PantallaSeleccion, añadimos ImageButton y queriamos usar SkinComposer pero salio todo mal y lo dejamos para otro momento.
- Creamos una carpeta locale para la traduccion del juego al Ingles o Español (si java detecta el pais, se cambia) y para manejar mejor la informacion de los personajes en la PantallaSeleccion mas que nada.
- Pusimos el bundle en el Render para que se use 1 solo en todo el juego (static). 
- Implementamos como va a ser el enum InfoPersonaje, ya sea para la info del personaje,valga la redundancia, y para reflection cuando hagamos lo de la seleccion del personaje. Tiene bastantes cosas.
- Añadimos el Desafios.MD.

### "empezando con la PantallaSeleccionPj" - Fecha 21/09

- Empezamos con la pantalla de seleccion de los personajes, cuando ponen "Jugar" en el menu.
- Hicimos el esqueleto de como va a ser el diseño de la pantalla, utilizando Scene2DUI.
- Creamos un enum para guardar todos los textos de la info de los Pj.
- Por ultimo, creamos otro package para poner todo lo relacionado a Box2D.

### "asunto personajes resuelto" - Fecha 18/09

- Resolvimos o mas bien el profe nos ayudo a resolver el error que teniamos con los saltos y las entradas de los Personajes.
- Ahora ya andan perfecto los 2 personajes y creamos otra clase hija para testearlo, faltaria hacer las texturas correspondientes. 

### "sonido corriendo, 2 pjs en pantalla y viewport HUD" - Fecha 10/09

- Añadimos un sonido cuando esta corriendo y lo implementamos en el codigo.
- Hicimos otros ajustes para que ya se puedan visualizar 2 pj en la pantalla (con algunas fallas a resolver).
- Pusimos un viewport al stage de HUD, ya que modificaba el que tenia la clase PantallaPvP, ahora ya anda joya.

*Aclaracion: Respecto a los 2 pj en pantalla, es para probar las colisiones y demas cosas, despues cuando veamos Redes, eso se modificara.*

### "adaptando clase Personaje a herencia" - Fecha 9/09

- En la clase Personaje (abstract), cambiamos lo debido para que se creen bien los diferentes Personajes que va a tener el juego.
- Creamos la clase Vin, la que seria uno de los personajes del juego.
- Cambiamos los nombres de los archivos, tambien para organizar (herencia).
- Cambiamos las rutas de las animaciones en la clase Recursos, ahora van a ser String[].

### "barra de vida hecho" - Fecha 9/09

- Añadimos y diseñamos la barra de vida.
- La implementamos en el codigo con Scene2DUI. 
- Añadimos el archivo respectivo (GIMP).

### "modificando Imagen,gitignore y creando .bat" - Fecha 4/09

- Modificamos la clase Imagen y Personaje para que el codigo quede mas limpio.
- Modificamos el gitignore (habia cosas que no queria que me oculte al subirlo).
- Creamos un .bat para ejecutar el juego con doble click (y no por consola).

### "agregando animacion de salto" - Fecha 3/09

- Hicimos tres texturas para el salto.
- Las cargamos y las implementamos en el codigo.
- Tambien añadimos archivos XCF faltantes.

### "empezando HUD" - Fecha 2/09

- Empezamos a hacer el HUD para la PantallaPvP. (investigamos acerca de Scene2D UI).
- Hicimos y añadimos icono y barra de salud.
- Creamos las carpetas en assets, hud y skin.

### "faroles" - Fecha 29/08

- Pusimos faroles en el background.  

### "clase Plataforma,background y README.md" - Fecha 26/08

- Hicimos la clase Plataforma y ya estan implementadas las mismas.
- Renovamos el background, cambiandole el piso y las paredes.
- Dimos vuelta todo el README.

### "subiendo texturas,animaciones de plataformas" - Fecha 26/08

- Subimos las texturas y las animaciones de las plataformas.

### "clase Audio,saltar(box2D),flip y ventana opciones" - Fecha 25/08

- Hicimos la clase Audio, que lamentablemente es static (porque todavia no lo vimos a fondo el concepto), funciona como organizador de todos los sonidos, no como reutilizable.
- Arreglamos lo del salto del pj en Box2D, que antes estaba mal hecho. Para esto creamos la clase Colision, nos va a servir de mucho.
- Creamos el enumerador UserData para ponerles un ID a los Bodies, esto para usarlo en la clase Colision.
- Arreglamos el flip del pj de nuevo!!! (era una boludez).
- Hicimos una clase VentanaOpciones para que sea reutilizable, tanto en el menu como en la pelea (todavia faltan algunas cosas).
- Ajustamos/mejoramos los limites del mapa y empezamos a tocar el tema de las plataformas.
*Recordatorio: Hacer las plataformas (animacion,codigo.)*

### "haciendo menu (apartado opciones)" - Fecha 24/08

- Hicimos la pestaña Opciones, para poner Pantalla Completa,Ventana,Subir y bajar volumen.
- Esta todo bien (adaptamos el mouse,las teclas,los sonidos,el bloqueo de las opciones principales), todo funcional.
- Añadimos el viewport, porque faltaba solo en esta pantalla hacerlo.
- Ahora tambien se puede saltar la pantalla de carga con el click, tambien modificamos algo en Config y pusimos el ESC en Entradas.
*Recordatorio: Habria que hacer una clase Audio para manipular todos los sonidos que estaran en el juego.*

### "back to the game (gran commit)" - Fecha 23/08

- Implementamos Box2D. (Añadimos la dependencia al build.gradle).
- Establecimos en el mundo (World) una conversion (PPM, esta explicado en el codigo). Llevo un tiempito entenderlo. Creamos una clase para eso.
- Tuvimos que cambiar muchas cosas debido al punto anterior.Camara,viewport,tamaño de las texturas.
- Creamos los Bodies con una clase que creamos (Fisica).
- Actualizamos el movimiento del personaje en base a Box2D.

### "tocando clase Animacion y flip PJ" - Fecha 8/08

- Cambiamos cosas de la clase Animacion para que sea mas reutilizable todavia.
- Ahora funciona lo de flipear el personaje, aunque sobrecargue mucho el metodo draw().

### "disposeando y cambie cancion" - Fecha 6/08

- Hicimos dispose() en todas las pantallas. (todo lo que sea texture,spriteBatch,bitMapFont).
- Cambiamos la cancion que tenia en el menu.
- Estuvimos comentando algunas cosas.

### "cambiando cosas del viewport" - Fecha: 4/08

- Cambiamos cosas del viewport, en 2 de las 3 pantallas del juego.
- La pantalla del menu la dejamos intacta.

### "implementacion animaciones, pantalla de carga" - Fecha: 30/07

- Implementamos las animaciones, andan bien (creamos la clase Animacion).
- Las animaciones las adaptamos al movimiento, si corre, hace la animacion de correr y si esta quieto, hace la animacion de quieto.
- La pantalla de carga ya esta terminada (añadimos el logo).

### "subiendo las animaciones de personaje" - Fecha:28/07

- Hicimos y cargamos las animaciones del personaje al repositorio.

### "haciendo mouse e implementando sonidos" - Fecha:27/07

- Hicimos para que el menu pueda ser usado tambien con el mouse (colision con rectangulos).
- Ya las opciones del menu accionan (te redirigen a otra pantalla).
- Pusimos sonido de cada vez que se selecciona una opcion.
- Pusimos musica en la pantalla de carga y menu.
- Resolvimos un problema que teniamos con la opacidad (canal alpha) del rectangulo del menu.
- Creamos la clase Config para sacar siempre de ahi el tamaño de la pantalla (ancho,alto).

### "terminando menu y clase I/O" - Fecha: 27/07

- Hicimos para que se seleccionen bien las opciones del menu (que cambien de color cuando es seleccionada una,tambien lo del delay).
- Tambien utilizamos ShapeRenderer para hacer el rectangulo, que va en el menu, falta ajustar la opacidad.
- Cambiamos las entradas de los personajes respecto al movimiento de estos. Ahora usamos la clase Entradas. (antes usabamos Gdx.input).
- Terminamos con las animaciones (quieto y correr), falta implementarlas en el juego.

### "haciendo movimiento menu y otras cosas" - Fecha: 25/07
- Hicimos el fade del menu y vuelve a su posicion actual (quedo bien).
- Avanzamos para la interaccion con el menu.
- Creacion de las clases Entradas,Texto,Recursos (relacionadas con el punto anterior).
- Avanzando en las animaciones del personaje (no estan en el repo).

### "empece el menu y el pj salta" - Fecha: 24/07
- El personaje salta.
- Empezamos a hacer el menu.
- Creamos otra camara para el menu, para hacer un movimiento
- Cargamos otro personaje (mejor hecho) y cargamos un background para el menu.

### "primer commit"
- Creamos y cargamos un mapa.
- Creamos y cargamos un personaje.
- Hicimos el moviemiento del personaje (izq,der).
- Creamos una camara para hacer el viewport (probamos con la misma el seguimiento del pj y el zoom).
























