# Truck Simulator

## Project Overview

This project is a Truck Management Simulator developed in Java for the Data Structures and Algorithms course. The application efficiently manages a fleet of trucks with varying capacities, assigns them to parking lots, and handles job assignments such as loading and movement between lots.

# Features

## 1. Truck Management

Trucks have unique integer IDs and predefined capacities.

Trucks cannot be loaded beyond their maximum capacity.

## 2. Parking Lot System

Parking lots have a capacity constraint, limiting which trucks can be assigned.

Each lot has waiting and ready sections for trucks.

A truck must be in the ready section to move to another lot.

If a lot is full, the truck is assigned to the next smaller capacity lot if available.

## 3. Loading System

Loads are assigned to trucks in the ready section.

If a truck reaches maximum capacity, it moves to a new parking lot.

Excess load is transferred to the next available parking lot.

## 4. Operations & Sorting

Trucks can be moved, loaded, and sorted according to their capacity.

The system ensures optimal truck assignment for efficiency.

Parking lots and trucks can be created or deleted dynamically.

# Input & Output

The program processes an input file with commands for truck and parking lot management.

A log file is generated to record the system’s operations and outputs.

## Example Commands

create_parking_lot 50 15

add_truck 1 100

ready 100

load 100 50

delete_parking_lot 50

count 50


## Example Log Output

Truck 1 added to parking lot with capacity 100.

Truck 1 moved to ready section.

Truck 1 loaded with 50 units.

Truck 1 reached full capacity and moved to lot 50.


## Implementation Details

Efficient Data Structures: Optimized for handling large truck fleets.

Sorting & Priority Handling: Ensures optimal truck assignment.

Load Balancing Algorithm: Evenly distributes loads among available trucks.

# Running the Project

## Compilation & Execution

javac *.java
java Main <input_file> <output_file>

<input_file>: Path to the input file containing commands.

<output_file>: Path to save the generated log output.

# Notes

Execution time is optimized for handling large datasets.

Strict capacity constraints and efficient allocation are ensured.

All outputs follow the expected format precisely.


