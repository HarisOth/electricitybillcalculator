# KiraBill - Electricity Bill Calculator

## Overview
KiraBill is an Android application designed for calculating monthly electricity bills. Developed as a mobile technology assignment, this app showcases practical implementation of Android development concepts with a focus on user experience and data management.

## Features
- **Accurate Bill Calculation** – Uses standard electricity tariffs for precise calculations  
- **Intuitive Month Selection** – Dropdown interface for easy month selection  
- **Dynamic Rebate Adjustment** – Slider for 0% to 5% rebate with real-time updates  
- **Local Data Storage** – SQLite database for offline access  
- **Complete History Tracking** – View all past calculations  
- **Edit & Delete Functionality** – Full CRUD operations for managing records  
- **Professional UI Design** – Card-based material design interface  
- **Student Information Section** – Includes academic details and project information  

## Architecture
- **Development Language**: Java  
- **Minimum SDK**: API 21 (Android 5.0 Lollipop)  
- **Database**: SQLite (Local Storage)  
- **Design Pattern**: Model-View-Controller (MVC)  
- **Development Environment**: Android Studio  
- **Version Control**: Git & GitHub  

## Tariff Calculation
The electricity units are charged based on the block used. Refer to the table for the rate.

| Block | Charges (sen/kWh) |
|-------|-------------------|
| For the first 200 kWh (1 - 200 kWh) per month | 21.8 |
| For the next 100 kWh (201 - 300 kWh) per month | 33.4 |
| For the next 300 kWh (301 - 600 kWh) per month | 51.6 |
| For the next 300 kWh (601 - 1000 kWh) per month onwards | 54.6 |

**Calculation Formula**:  
`Total Charges = Sum of (Block Usage × Block Rate)`  
`Final Cost = Total Charges - (Total Charges × Rebate Percentage / 100)`

## Application Screens

### Main Interface
| Splash Screen | Main Page |
|---------------|-----------|
| ![Splash Screen](screenshots/ss1.png) | ![Main Page](screenshots/ss2.png) |

### Data Management
| History Overview | Bill Details | Edit Record |
|------------------|---------------|--------------|
| ![History Overview](screenshots/ss3.png) | ![Bill Details](screenshots/ss4.png) | ![Edit Record](screenshots/ss5.png) |

### Information & About
| About Section | GitHub Integration |
|---------------|--------------------|
| ![About Section](screenshots/ss6.png) | ![GitHub Integration](screenshots/ss7.png) |

## Installation
1. Click **"Code"** → **"Download ZIP"** on GitHub  
2. Extract the downloaded ZIP file  
3. Open **Android Studio** → Click **"Open"**  
4. Select the extracted project folder  
5. Wait for Gradle sync to complete  
6. Connect device or start emulator  
7. Click **Run** (▶) to install and launch  
