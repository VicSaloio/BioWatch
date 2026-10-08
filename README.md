# BioWatch

**Observe. Registre. Preserve.**

BioWatch é um aplicativo Android nativo para registro e acompanhamento da biodiversidade local, desenvolvido com Kotlin e Jetpack Compose.

O aplicativo permite registrar observações de espécies, consultar informações taxonômicas, armazenar os dados localmente, utilizar geolocalização e visualizar os registros em mapa.

## Funcionalidades

- Registro de observações de biodiversidade
- Consulta de espécies
- Armazenamento local dos registros
- Inclusão de foto nas observações
- Geolocalização
- Conversão de coordenadas em endereço
- Visualização dos registros em mapa
- Tela de detalhes das observações
- Suporte a português e inglês

## Tecnologias utilizadas

- Kotlin
- Jetpack Compose
- Material 3
- Android Studio
- Room
- Retrofit
- APIs REST
- FusedLocationProviderClient
- MapLibre
- Git e GitHub

## Serviços externos

O BioWatch utiliza serviços externos para complementar as informações exibidas no aplicativo:

- **iNaturalist** — consulta de informações sobre espécies
- **OpenStreetMap / Nominatim** — dados de localização e geocodificação
- **OpenFreeMap / MapLibre** — visualização do mapa

## Armazenamento de dados

As observações registradas pelo usuário são armazenadas localmente no dispositivo utilizando **Room**.

Os dados das observações não são enviados ao iNaturalist. A plataforma é utilizada apenas para consulta de informações taxonômicas.

## Telas do aplicativo

O BioWatch possui seis telas principais:

1. Home
2. Nova Observação
3. Registros
4. Detalhes da Observação
5. Mapa
6. Perfil

## Idiomas

O aplicativo possui suporte para:

- Português (Brasil)
- Inglês

O idioma é definido de acordo com a configuração do dispositivo.

## Contexto do projeto

Projeto desenvolvido durante o curso de **Análise e Desenvolvimento de Sistemas na FIAP**, com foco na aplicação prática de conceitos de desenvolvimento Android nativo, persistência local de dados, integração com APIs, geolocalização e navegação entre telas.

## Autor

**Victor Lance Saloio**

- GitHub: [VicSaloio](https://github.com/VicSaloio)
- LinkedIn: [victor-saloio](https://linkedin.com/in/victor-saloio)
