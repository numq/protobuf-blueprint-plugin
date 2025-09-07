[![Version](https://img.shields.io/jetbrains/plugin/v/21792-protobuf-blueprint.svg)](https://plugins.jetbrains.com/plugin/21792-protobuf-blueprint)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/21792-protobuf-blueprint.svg)](https://plugins.jetbrains.com/plugin/21792-protobuf-blueprint)

<p align="center""><img src="media/logo.png" alt="logo" height="128px"/></p>

<div align="center" style="display: grid; justify-content: center;">

|                                                                  🌟                                                                   |                  Support this project                   |               
|:-------------------------------------------------------------------------------------------------------------------------------------:|:-------------------------------------------------------:|
|  <img src="https://raw.githubusercontent.com/ErikThiart/cryptocurrency-icons/master/32/bitcoin.png" alt="Bitcoin (BTC)" width="32"/>  | <code>bc1qs6qq0fkqqhp4whwq8u8zc5egprakvqxewr5pmx</code> | 
| <img src="https://raw.githubusercontent.com/ErikThiart/cryptocurrency-icons/master/32/ethereum.png" alt="Ethereum (ETH)" width="32"/> | <code>0x3147bEE3179Df0f6a0852044BFe3C59086072e12</code> |
|  <img src="https://raw.githubusercontent.com/ErikThiart/cryptocurrency-icons/master/32/tether.png" alt="USDT (TRC-20)" width="32"/>   |     <code>TKznmR65yhPt5qmYCML4tNSWFeeUkgYSEV</code>     |

</div>

<br>

<p align="center">
<!-- Plugin description -->
**Protobuf Blueprint** is an Intellij Idea plugin that allows you to generate *Protocol Buffers* code using simple
format.
<!-- Plugin description end -->
</p>

<br>

<p align="center""><img src="media/preview.png" alt="logo""/></p>

## Installation

- Using IDE built-in plugin system:

  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "Protobuf
  Blueprint"</kbd> >
  <kbd>Install Plugin</kbd>

- Manually:

  Download the [latest release](https://github.com/numq/protobuf-blueprint-plugin/releases/latest) and install it manually
  using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>

## Usage

1. Go to <b>Tools -> Protobuf Blueprint</b>  to open plugin tool window.
2. Follow the format hint in the plugin tool window to generate the code.

### Format Requirements:

> Break the line to complete the input.

Message:
> Use *PascalCase* for **message names**.

```message messageName messageName messageName```

Enum:
> Use *PascalCase* for **enum name** and *CAPITALS_WITH_UNDERSCORES* for **enum values**.

```enum enumName enum_value enum_value enum_value```

Service:
> Use *PascalCase* for **service name** and **rpc names**.

```service serviceName rpcName rpcName rpcName```

![preview-full](./media/preview-full.png)
