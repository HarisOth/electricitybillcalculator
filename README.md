# ⚡ Electricity Bill Calculator 2

Android application for calculating monthly electricity bills based on TNB tariff rates. Developed for ICT602 Mobile Technology assignment.

## 📱 Features
- **Calculate Electricity Bill** with TNB block tariff system
- **Month Selection** (January - December) via spinner
- **Adjustable Rebate** (0% - 5%) using seek bar
- **Local Database Storage** using SQLite to save calculation history
- **Detailed View** of each bill calculation
- **User-Friendly Interface** with custom theme and icons

## 🏗️ Architecture
- **Language**: Java
- **Minimum SDK**: API 21 (Android 5.0)
- **Database**: SQLite (Local/Offline)
- **Architecture**: MVC Pattern
- **Tools**: Android Studio, Git, GitHub

## 📊 Tariff Calculation Method
Based on TNB tariff blocks (sen/kWh):
| Block | Rate (sen/kWh) |
|-------|----------------|
| 1-200 kWh | 21.8 |
| 201-300 kWh | 33.4 |
| 301-600 kWh | 51.6 |
| 601-900+ kWh | 54.6 |

**Formula**: `Final Cost = Total Charges - (Total Charges × Rebate %)`

## 📸 Application Screenshots
*(Add screenshots here later. You can upload images to GitHub and link them)*

## 🚀 Installation & Usage
1. **Clone the repository**
   ```bash
   git clone https://github.com/HarisOth/electricitybillcalculator2.git
