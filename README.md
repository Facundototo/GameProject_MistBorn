# GameProject_MistBorn
Proyecto final de la materia "programacion sobre redes". 

## Commits

### "primer commit"
- Creamos y cargamos un mapa.
- Creamos y cargamos un personaje.
- Hicimos el moviemiento del personaje (izq,der).
- Creamos una camara para hacer el viewport (probamos con la misma el seguimiento del pj y el zoom).

### "empece el menu y el pj salta" - Fecha: 24/07
- El personaje salta.
- Empezamos a hacer el menu.
- Creamos otra camara para el menu, para hacer un movimiento
- Cargamos otro personaje (mejor hecho) y cargamos un background para el menu.

### "haciendo movimiento menu y otras cosas" - Fecha: 25/07
- Hicimos el fade del menu y vuelve a su posicion actual (quedo bien).
- Avanzamos para la interaccion con el menu.
- Creacion de las clases Entradas,Texto,Recursos (relacionadas con el punto anterior).
- Avanzando en las animaciones del personaje (no estan en el repo).

### "terminando menu y clase I/O" - Fecha: 27/07

- Hicimos para que se seleccionen bien las opciones del menu (que cambien de color cuando es seleccionada una,tambien lo del delay).
- Tambien utilizamos ShapeRenderer para hacer el rectangulo, que va en el menu, falta ajustar la opacidad.
- Cambiamos las entradas de los personajes respecto al movimiento de estos. Ahora usamos la clase Entradas. (antes usabamos Gdx.input).
- Terminamos con las animaciones (quieto y correr), falta implementarlas en el juego.

### "haciendo mouse e implementando sonidos" - Fecha:27/07

- Hicimos para que el menu pueda ser usado tambien con el mouse (colision con rectangulos).
- Ya las opciones del menu accionan (te redirigen a otra pantalla).
- Pusimos sonido de cada vez que se selecciona una opcion.
- Pusimos musica en la pantalla de carga y menu.
- Resolvimos un problema que teniamos con la opacidad (canal alpha) del rectangulo del menu.
- Creamos la clase Config para sacar siempre de ahi el tamaño de la pantalla (ancho,alto).

### "subiendo las animaciones de personaje" - Fecha:28/07

- Hicimos y cargamos las animaciones del personaje al repositorio.

### "implementacion animaciones, pantalla de carga" - Fecha: 30/07

- Implementamos las animaciones, andan bien (creamos la clase Animacion).
- Las animaciones las adaptamos al movimiento, si corre, hace la animacion de correr y si esta quieto, hace la animacion de quieto.
- La pantalla de carga ya esta terminada (añadimos el logo).

### "cambiando cosas del viewport" - Fecha: 4/08

- Cambiamos cosas del viewport, en 2 de las 3 pantallas del juego.
- La pantalla del menu la dejamos intacta.

### "disposeando y cambie cancion" - Fecha 6/08

- Hicimos dispose() en todas las pantallas. (todo lo que sea texture,spriteBatch,bitMapFont).
- Cambiamos la cancion que tenia en el menu.
- Estuvimos comentando algunas cosas.

### "tocando clase Animacion y flip PJ" - Fecha 8/08

- Cambiamos cosas de la clase Animacion para que sea mas reutilizable todavia.
- Ahora funciona lo de flipear el personaje, aunque sobrecargue mucho el metodo draw(). 

### "back to the game (gran commit)" - Fecha 23/08

- Implementamos Box2D. (Añadimos la dependencia al build.gradle).
- Establecimos en el mundo (World) una conversion (PPM, esta explicado en el codigo). Llevo un tiempito entenderlo. Creamos una clase para eso.
- Tuvimos que cambiar muchas cosas debido al punto anterior.Camara,viewport,tamaño de las texturas.
- Creamos los Bodies con una clase que creamos (Fisica).
- Actualizamos el movimiento del personaje en base a Box2D.

### "haciendo menu (apartado opciones)" - Fecha 24/08

- Hicimos la pestaña Opciones, para poner Pantalla Completa,Ventana,Subir y bajar volumen.
- Esta todo bien (adaptamos el mouse,las teclas,los sonidos,el bloqueo de las opciones principales), todo funcional.
- Añadimos el viewport, porque faltaba solo en esta pantalla hacerlo.
- Ahora tambien se puede saltar la pantalla de carga con el click, tambien modificamos algo en Config y pusimos el ESC en Entradas.

* Recordatorio: Habria que hacer una clase Audio para manipular todos los sonidos que estaran en el juego.*

### "clase Audio,saltar(box2D),flip y ventana opciones" - Fecha 25/08

- Hicimos la clase Audio, que lamentablemente es static (porque todavia no lo vimos a fondo el concepto), funciona como organizador de todos los sonidos, no como reutilizable.
- Arreglamos lo del salto del pj en Box2D, que antes estaba mal hecho. Para esto creamos la clase Colision, nos va a servir de mucho.
- Creamos el enumerador UserData para ponerles un ID a los Bodies, esto para usarlo en la clase Colision.
- Arreglamos el flip del pj de nuevo!!! (era una boludez).
- Hicimos una clase VentanaOpciones para que sea reutilizable, tanto en el menu como en la pelea (todavia faltan algunas cosas).
- Ajustamos/mejoramos los limites del mapa y empezamos a tocar el tema de las plataformas.
* Recordatorio: Hacer las plataformas (animacion,codigo.)*

* Los objetivos actuales estaran escritos en el codigo, a medida que los vayamos haciendo los borraremos y pondremos otros.*

* Para ejecutar este juego en el cmd, en la carpeta raiz del juego(cd "carpeta"), hay que ejecutar este comando: gradlew desktop:run * 

