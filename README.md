# Korean Patch

> ## ⚠️ 이 저장소는 비공식 수정본입니다 / Unofficial modified version
>
> 원본: **[najoan125/KoreanPatch-multiLoader](https://github.com/najoan125/KoreanPatch-multiLoader)** (원저작자 Najoan)
> This is a modified fork. The original project is linked above.
>
> **수정 내용 / Modifications**
> - 2026-08-01: Minecraft **26.2** 대응 포팅 (`26.2` 브랜치) — Ported to Minecraft 26.2
> - Fabric 전용. NeoForge 모듈 및 일부 모드 호환 믹스인(REI, Axiom, Xaeros,
>   Command Block IDE, BetterCommandBlockUI, Easy Anvils)은 제외했습니다.
>   Fabric only; the NeoForge module and some mod-compat mixins were removed.
> - 네이티브 바이너리(`native/*.dylib`, `*.dll`)는 **재빌드하지 않고 원본 1.9.6의 파일을
>   그대로 사용**했습니다. SHA-256이 원본과 동일합니다.
>   Native binaries are byte-identical to the upstream 1.9.6 release (not rebuilt).
>
> 원저작자의 공식 릴리스가 아닙니다. 문제 발생 시 원본 저장소가 아니라 이 저장소의
> Issues에 남겨주세요.
> This is not an official release. Please report issues here, not upstream.
>
> 라이선스는 원본과 동일한 **LGPL-3.0**을 유지합니다. / Licensed under LGPL-3.0, same as upstream.

## 이 모드는 어떤 모드인가요? What mod is this?

Korean Patch는 마인크래프트에서 한글 채팅을 더 원활하게 사용할 수 있도록 하는 Fabric/Forge/NeoForge 모드입니다.<br>
Korean Patch is a Fabric/Forge/NeoForge mod that allows you to use Korean chat more smoothly in Minecraft.

This mod uses edited [CocoaInput-lib](https://github.com/najoan125/CocoaInput-lib).

![indicator](https://cdn.modrinth.com/data/qyulnpBL/images/57ad8ed4ff75acd2dcb88459565d50692c1809e5.png)

## 플랫폼별 한/영 변환 방법 Korean/English conversion method by platform

플랫폼별 변환 키를 눌러 한국어/영어 모드 변경이 가능합니다.<br>
You can change Korean/English mode by pressing the conversion key for each platform.

| Platform (OS)               | Toggle Lang Key             | Toggle IME Key           |
|------------------------|---------------------|--------------------|
| Windows                | `한/영 (Right-Alt)` | `Left-Control + I` |
| Mac  | `Left-Control`      | `지원하지 않음 (Not support)` |
| Linux | `Left-Control`      | `지원하지 않음 (Not support)` |

(언어 변환 키와 IME 변환 키는 키바인딩 설정에서 변경할 수 있습니다.) 

## 문제 Issues

이 모드를 사용하는 데 문제가 있다면 [Issues](https://github.com/najoan125/KoreanPatch-multiLoader/issues)에 작성해주세요.<br>
If you encounter any issues using this mode, please post them on [Issues](https://github.com/najoan125/KoreanPatch-multiLoader/issues).

## OpenSource License
- [NaraeChat](https://github.com/sokcuri/NaraeChat)
  - LGPL 3.0
  - Referenced to the Hangul_Set_2_Layout, KeyboardLayout and QwertyLayout classes of the Keyboard package and HangulProcessor class.
- [caramelChat](https://github.com/LemonCaramel/caramelChat)
  - LGPL 3.0
  - Referenced to the IOperator and arch package of the driver package.

## 기여자 Contributors
이 프로젝트에 기여한 분들<br>
People who contributed to this project

- [Shihyeon](https://github.com/Shihyeon)
