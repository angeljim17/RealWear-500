# RealWear Navigator — Capacitación industrial manos libres

Aplicación Android para **RealWear Navigator 200/500** orientada a capacitación industrial. Reproduce una secuencia de videos de ensamble y permite controlarlos por voz sin usar las manos.

## Stack

- Kotlin
- Android (ExoPlayer, Media3)
- RealWear HF (comandos de voz)
- Lottie (animación de finalización)

## Funcionalidades

- Reproducción secuencial de videos de capacitación (introducción + pasos 1–6)
- Control por voz: **"Siguiente video"** y **"Repetir video"**
- Navegación manos libres para entornos industriales
- Animación Lottie al completar la secuencia

## Comandos de voz

| Comando | Acción |
|---------|--------|
| Siguiente video | Avanza al siguiente paso |
| Repetir video | Reinicia el video actual |

## Cómo ejecutar

1. Abre el proyecto en **Android Studio**
2. Conecta un dispositivo RealWear Navigator o emulador compatible
3. Ejecuta la app (`Run`)

Los videos están en `app/src/main/res/raw/`.

## Autor

**Ángel Jiménez Morales** — [GitHub](https://github.com/angeljim17)
