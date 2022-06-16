# Paranoid Social Network

## Como obtener el código fuente

La forma mas sencilla de obtener el código fuente es a traves de GIT.

```
git clone https://github.com/RubenRubens/paranoid_android.git
cd paranoid_android
```

## Como ejecutar este proyecto

Si queremos utilizar el servidor `https://paranoid.rubenarriazu.com/`
se puede hacer cambiando a la rama _master_.

```
git switch master
```

El siguiente paso es abrir Android Studio y ejecutar la tarea _app_ bien
en un emulador, bien en hardware real.

Si disponemos de un servidor propio, la forma de hacerlo es desde la rama
_develop_.

## Como ejecutar los tests

Este proyecto cuenta con tests para comprobar el correcto funcionamiento
de la API y Retrofit. El código Java de estos tests se ejecuta en nuestra
maquina como cualquier otro código. Es decir, no se ejecuta en la plataforma
Android por que no se ha considerado necesario. Para ello hay que ejecutar
la tarea _APITest_.

El otro test que hay comprueba el almacenamiento de un XML encriptado en el
dispositivo Android. La tarea para ejecutarla el test se llama _CredentialsStorageTest_.
