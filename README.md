[![Version](https://img.shields.io/jetbrains/plugin/v/21792-protobuf-blueprint.svg)](https://plugins.jetbrains.com/plugin/21792-protobuf-blueprint)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/21792-protobuf-blueprint.svg)](https://plugins.jetbrains.com/plugin/21792-protobuf-blueprint)

![logo](./media/logo.png)

<!-- Plugin description -->
**Protobuf Blueprint** is an Intellij Idea plugin that allows you to generate Protocol Buffers code using simple
format.
<!-- Plugin description end -->

![preview](./media/preview.png)

## Installation

- Using IDE built-in plugin system:

Settings/Preferences > Plugins > Marketplace > Search for "Protobuf Blueprint" > Install Plugin

- Manually:

Download the latest release and install it manually using Settings/Preferences > Plugins > ⚙️ > Install plugin from
disk...

## Usage

1. Go to Tools -> Protobuf Blueprint to open plugin tool window.

2. Follow the format hint in the plugin tool window to generate the code.

### Format Requirements:

Break the line to complete the input.

#### Syntax:

Use exactly _syntax_ keyword followed by the version name.

```syntax versionName```

#### Package:

Use exactly _package_ keyword followed by the package name.

```package packageName```

#### Message:

Use _PascalCase_ for message names.

```message messageName messageName messageName```

#### Enum:

Use _PascalCase_ for enum name and _CAPITALS_WITH_UNDERSCORES_ for enum values.

```enum enumName enum_value enum_value enum_value```

#### Service:

Use _PascalCase_ for service name and rpc names.

```service serviceName rpcName rpcName rpcName```

![preview-full](./media/preview-full.png)
