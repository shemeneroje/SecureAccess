// Function to generate a strong, random password
export function generateStrongPassword(length = 16) {
  const lowercase = 'abcdefghijklmnopqrstuvwxyz';
  const uppercase = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';
  const numbers = '0123456789';
  const special = '!@#$%^&*()-_+=<>?';
  
  // Combine all character sets
  const allChars = lowercase + uppercase + numbers + special;
  
  let password = '';
  
  // Ensure the password contains at least one character from each set for strength
  password += lowercase.charAt(Math.floor(Math.random() * lowercase.length));
  password += uppercase.charAt(Math.floor(Math.random() * uppercase.length));
  password += numbers.charAt(Math.floor(Math.random() * numbers.length));
  password += special.charAt(Math.floor(Math.random() * special.length));

  // Fill the rest of the password length randomly
  for (let i = password.length; i < length; i++) {
    password += allChars.charAt(Math.floor(Math.random() * allChars.length));
  }
  
  // Shuffle the password string to randomize the character order
  password = password.split('').sort(() => 0.5 - Math.random()).join('');
  
  return password;
}

// Initial mock data for the application state
export const initialPasswords = [
  { id: 1, name: 'Gmail', username: 'user@gmail.com', url: 'https://gmail.com', category: 'Email', password: 'securepassword1', notes: 'Primary account' },
  { id: 2, name: 'GitHub', username: 'developer123', url: 'https://github.com', category: 'Development', password: 'securepassword2', notes: 'Two-factor enabled' },
  { id: 3, name: 'Netflix', username: 'movie.lover@email.com', url: 'https://netflix.com', category: 'Entertainment', password: 'securepassword3', notes: '' },
  { id: 4, name: 'Amazon', username: 'shopper@email.com', url: 'https://amazon.com', category: 'Shopping', password: 'securepassword4', notes: 'Do not share' },
];
