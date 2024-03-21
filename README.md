# BTC Scheduler

BTC Scheduler is a robust cron scheduler designed to automate BTC purchases using the Bitvavo crypto broker.

## Table of Contents
- [Features](#features)
- [Setup](#setup)
- [Limitations](#limitations)
- [Usage](#usage)
- [Security Information](#security-information)
- [License](#license)

## Features

- **Schedule BTC Purchases**: Automate your BTC purchases using a simple cron format.
- **Manage Schedules**: Easily create, delete, and list all your existing schedules.

## Setup

Setting up BTC Scheduler is straightforward. You just need to start the Docker container and add the credentials from bitvavo.

Commands:
```bash
docker-compose build
docker-compose up -e API_KEY=your_api_key API_SECRET=your_api_secret
```

If you need help for the Docker Installation, please refer to the [Docker Installation Guide](DockerInstallation.md).

## Limitations

- BTC Scheduler is designed for single-user use.
- Currently, only BTC purchases can be scheduled.

## Usage

Interact with the scheduler using HTTP calls. Here are the available options:

- **Create a Schedule**: Create a new schedule using the cron format.
- **Delete a Schedule**: Delete an existing schedule.
- **List All Schedules**: List all the currently active schedules.

The scheduler will execute the BTC purchase at the scheduled times using the Bitvavo crypto broker.

## Security Information

Please note that this service is not designed to run online and therefore lacks certain security measures. It serves as a simple example of a cron job scheduler.

## License

This project is licensed under the terms of the MIT license. For more details, see the [LICENSE](LICENSE) file in the project root.