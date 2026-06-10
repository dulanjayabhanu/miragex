# MirageX

A secure, offline, console-based file encryption tool built with Java 21 and Picocli.
MirageX is designed to protect sensitive plain text files stored on your local machine,
such as account credentials, API keys, personal notes, and any other data you want
to keep private.

<br>

## Why MirageX

Most people store sensitive information in plain text files on their machines. These files
are vulnerable to unauthorized access, accidental exposure, or being read by third-party
tools such as AI coding agents, sync services, or backup software.

MirageX solves this by encrypting those files using industry-standard cryptographic
algorithms, ensuring that even if someone gains access to the file, the content remains
completely unreadable without the correct password and step count.

> If you work with AI coding agents such as GitHub Copilot, Cursor, or similar tools,
> those agents may have access to files in your working directory. By encrypting your
> sensitive files with MirageX before starting a session, you ensure that even if the
> agent reads the file, the content is protected. The password and step count never
> leave your hands.

<br>

## Security Model

MirageX is built on production-grade, industry-standard cryptographic primitives.
There are no custom or experimental algorithms involved.

| Component | Implementation |
|---|---|
| Encryption | AES-256-GCM (Authenticated Encryption) |
| Key Derivation | Argon2id (winner of the Password Hashing Competition) |
| Integrity | GCM authentication tag (detects tampering) |
| Randomness | Cryptographically secure random salt and IV per encryption |
| Provider | Bouncy Castle (audited, widely adopted crypto library) |

Every encryption operation generates a unique salt and initialization vector.
Encrypting the same file twice with the same password will produce different output
each time. This prevents pattern analysis attacks.

The GCM authentication tag ensures that if the encrypted file is modified or corrupted
in any way, decryption will fail immediately. MirageX will never silently return
wrong or garbage content.

<br>

## Trust and Transparency

MirageX is built with transparency as a core principle.

- The full source code is available in this repository for anyone to review
- There are zero outbound network calls. 
- The tool operates entirely on your local file system
- No data is collected, logged, or transmitted in any form
- All cryptographic operations are performed using Bouncy Castle, a well-known,
  open-source, and audited cryptography library

You are encouraged to review the source code, inspect the dependencies, and build
the tool from source before using it. Nothing is hidden.

<br>

## Features

- AES-256-GCM authenticated encryption
- Argon2id password-based key derivation with configurable iteration count
- Unique salt and IV generated per encryption operation
- Built-in tamper detection via GCM authentication
- Custom binary file header with magic bytes and version validation
- Three focused commands: lock, read, unlock
- Colored terminal output for clear feedback
- Fully offline, no installation beyond Java runtime required
- Cross-platform: runs on Windows, Linux, and macOS

<br>

## Requirements

- Java 21 or higher
- Any terminal or command prompt

<br>

## Installation

**Step 1 - Download**

Download the latest release ZIP for your operating system from the
[Releases](https://github.com/dulanjayabhanu/miragex/releases) page.

- miragex-1.0.0-linux-mac.zip (Linux and macOS)
- miragex-1.0.0-windows.zip (Windows)

Extract the ZIP. Both files inside must remain in the same folder.

**Step 2 - Add to PATH (one time only)**

This allows you to run `miragex` from any directory on your machine.

Linux and macOS:

First make the wrapper script executable. This is a one time step:

```bash
chmod +x /path/to/miragex/folder/miragex

# Example:
# chmod +x /Users/john/tools/MirageX/miragex
```

Then add to PATH permanently by adding this line to `~/.bashrc` (Linux) or `~/.zshrc` (macOS):

```bash
export PATH=$PATH:/path/to/miragex/folder

# Example:
# export PATH=$PATH:/Users/john/tools/miragex
```

Apply the changes:

```bash
source ~/.bashrc

# or for macOS
source ~/.zshrc
```

Windows PowerShell:

```powershell
[Environment]::SetEnvironmentVariable("Path", $env:Path + ";C:\path\to\miragex", "User")

# Example:
# [Environment]::SetEnvironmentVariable("Path", $env:Path + ";C:\Tools\miragex", "User")
```

Restart PowerShell after running this command.

**Step 3 - Verify**

```bash
miragex --version
```

<br>

## Usage

The core workflow is simple. Open a terminal from the directory where your target
file is located, then run the appropriate command.

### Encrypt a file

```bash
miragex lock <file-name> <password> <step-count>
```

Example:

```bash
cd D:\Development\Accounts

miragex lock secrets.txt mypassword 5
```

If `password` and `step` count are not provided as arguments, MirageX will prompt
you to enter them interactively. The `password` input is **hidden** for security.

Example with arguments:
```bash
miragex lock secrets.txt mypassword 3
```

Example with interactive prompt:
```bash
miragex lock secrets.txt
# Enter password:
# Enter step count (recommended 3-5):
```

This will create `secrets.mgx` in the same directory. The original file is not deleted.

### Decrypt to terminal

```bash
miragex read <file-name> <password> <step-count>
```

Example:

```bash
cd D:\Development\Accounts

miragex read secrets.mgx mypassword 5
```

Example with interactive prompt:
```bash
cd D:\Development\Accounts

miragex read secrets.mgx
# Enter password:
# Enter step count (recommended 3-5):
```

The decrypted content is printed directly to the terminal. No file is written.

> **Security Note:** The decrypted content is printed directly to the terminal and is
> visible in the scrollback buffer until the terminal is closed or cleared. After reading
> sensitive content, clear the terminal immediately.
>
> Linux and macOS:
> ```bash
> clear
> ```
> Windows PowerShell:
> ```powershell
> cls
> ```
> Also ensure terminal session logging is disabled in your terminal application settings.

### Decrypt to file

```bash
miragex unlock <file-name> <password> <step-count>
```

Example:

```bash
cd D:\Development\Accounts

miragex unlock secrets.mgx mypassword 5
```

Example with interactive prompt:
```bash
cd D:\Development\Accounts

miragex unlock secrets.mgx
# Enter password:
# Enter step count (recommended 3-5):
```

This will recreate the original `secrets.txt` file in the same directory.

<br>

## Step Count

The step count controls the number of Argon2id iterations used during key derivation.
A higher step count means more computation is required to derive the encryption key,
making brute force attacks significantly harder.

| Step Count | Strength | Use Case |
|---|---|---|
| 1 - 2 | Basic | Quick testing |
| 3 - 5 | Strong | General use (recommended) |
| 6 - 10 | Very strong | Highly sensitive data |

Higher step counts will increase the time taken to encrypt and decrypt. This is
intentional and is a property of the Argon2id algorithm.

<br>

## Important Warning

**Do not store your password or step count on the same machine as the encrypted file.**

The security of MirageX depends entirely on the secrecy of your password and step count.
If an attacker obtains both the encrypted file and the password, the file can be decrypted.

Recommended practice:

- Use a strong, unique password that you have memorized
- Store the password in a separate, secure location if you must write it down
- Never save the password in a text file on the same machine
- Never commit the password or step count to a version control system

<br>

## File Format

Encrypted files use the `.mgx` extension. Each file contains a binary header followed
by the AES-256-GCM ciphertext.

- [0-3]   Magic number  : MRGX
- [4]     Version       : 0x01
- [5-20]  Salt          : 16 bytes (random)
- [21-32] IV            : 12 bytes (random)
- [33+]   Ciphertext    : AES-256-GCM encrypted content

<br>

## Building from Source

```bash
git clone https://github.com/dulanjayabhanu/miragex.git

cd miragex

mvn package
```

The JAR will be available at `target/miragex.jar`.

To run tests:

```bash
mvn test
```

<br>

## Tech Stack

| Technology | Version | Purpose |
|---|---|---|
| Java | 21 | Runtime |
| Picocli | 4.7.6 | CLI framework |
| Bouncy Castle | 1.78.1 | Cryptography provider |
| JUnit 5 | 5.10.2 | Unit testing |
| Maven | 3.x | Build tool |

<br>

## License

MIT License. See [LICENSE](LICENSE) for details.