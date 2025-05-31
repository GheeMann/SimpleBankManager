# SimpleBankManager

This is a simple bank management system built in Java using the `RandomAccessFile` class. It stores and manages fixed-size bank records in a binary file and supports basic operations like adding, viewing, modifying, deleting, and searching bank accounts.

## 📌 Features

- Add new bank account records
- View all existing (non-deleted) records
- Modify an existing record by account number
- Delete a record (marks it as deleted)
- Search for a specific account
- Persistent file storage using `RandomAccessFile`

## 📂 Data Structure

Each record includes:
- `int` Account Number
- `String` Name (30 characters, fixed length)
- `double` Balance

Records are stored in binary format with fixed sizes for consistency and random access.

## 💡 What I Learned

- How to use `RandomAccessFile` for binary file I/O in Java
- Working with fixed-length records for structured data storage
- File seeking and reading/writing at specific positions
- Building a mini file-based database without using SQL
- Structuring Java classes with clear responsibilities

This project helped me understand how low-level file management works in Java and gave me hands-on experience with reading, writing, and modifying binary data using fixed offsets.