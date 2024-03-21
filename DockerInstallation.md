To install Docker and Docker Compose on a Debian system, you can follow these steps:

1. Update your existing list of packages:

```bash
sudo apt update
```

2. Install a few prerequisite packages which let `apt` use packages over HTTPS:

```bash
sudo apt install apt-transport-https ca-certificates curl software-properties-common
```

3. Add the GPG key for the official Docker repository to your system:

```bash
curl -fsSL https://download.docker.com/linux/debian/gpg | gpg --dearmor -o /usr/share/keyrings/docker-archive-keyring.gpg
```

3. Update the Docker repository to APT sources with the signed-by directive:
```bash   
echo "deb [arch=amd64 signed-by=/usr/share/keyrings/docker-archive-keyring.gpg] https://download.docker.com/linux/debian $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
```

4. Add the Docker repository to APT sources:

```bash
sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/debian $(lsb_release -cs) stable"
```

5. Update the package database with the Docker packages from the newly added repo:

```bash
sudo apt update
```

6. Make sure you are about to install from the Docker repo instead of the default Debian repo:

```bash
apt-cache policy docker-ce
```

7. Install Docker:

```bash
sudo apt install docker-ce
```

8. Docker should now be installed, the daemon started, and the process enabled to start on boot. Check that it's running:

```bash
sudo systemctl status docker
```

9. Install Docker Compose:

```bash
sudo curl -L "https://github.com/docker/compose/releases/download/1.29.2/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
```

10. Apply executable permissions to the Docker Compose binary:

```bash
sudo chmod +x /usr/local/bin/docker-compose
```

11. Test the installation:

```bash
docker-compose --version
```

This should display the version of Docker Compose that was installed.