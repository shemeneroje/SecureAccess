import React, { useState } from 'react';
import Sidebar from '../components/Sidebar.jsx';
import { QrCode, Lock, CheckCircle, Settings } from 'lucide-react';

// Placeholder component for QR code display (since we can't use external libraries)
const QRCodeDisplay = ({ value }) => (
    <div style={{ padding: '20px', backgroundColor: '#fff', borderRadius: '8px', display: 'inline-block' }}>
        <p style={{ color: '#2c3e50', margin: 0 }}>[Placeholder QR Code for: {value.substring(0, 15)}...]</p>
        <div style={{ width: '150px', height: '150px', border: '2px solid #3498db', marginTop: '10px', display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
            <QrCode size={48} color="#3498db" />
        </div>
    </div>
);

function Setup2FA({ passwords, onLogout }) {
    const [step, setStep] = useState(1); // 1: Generate QR, 2: Verify Code, 3: Success
    const [qrCodeData, setQrCodeData] = useState('');
    const [userInputCode, setUserInputCode] = useState('');
    const [statusMessage, setStatusMessage] = useState('');
    const [isVerifying, setIsVerifying] = useState(false);

    // Placeholder for backend call to generate a secret key and QR code URL
    const handleGenerate = () => {
        // This is where you call the Java backend API (GET /api/2fa/generate)
        const placeholderSecret = 'A_RANDOM_SECRET_FROM_BACKEND_1234567890';
        setQrCodeData(placeholderSecret); 
        setStep(2); // Move to verification step
        setStatusMessage('Scan the QR code below with your authenticator app.');
    };

    // Placeholder for backend call to verify the user's code
    const handleVerify = async (e) => {
        e.preventDefault();
        setIsVerifying(true);
        setStatusMessage('Verifying code...');
        
        // calling the Java backend API (POST /api/2fa/verify)
        // to send the code and check the authentication status back to the backend
        await new Promise(resolve => setTimeout(resolve, 1500));
        
        if (userInputCode === '123456') { // Simulate successful verification
            setStep(3);
            setStatusMessage('Two-Factor Authentication is now active!');
        } else {
            setStatusMessage('Verification failed. Please check the code and try again.');
        }
        setIsVerifying(false);
    };

    return (
        <div className="main-layout">
            <Sidebar passwordCount={passwords.length} onLogout={onLogout} />
            
            <div className="content-area" style={{ maxWidth: '800px', margin: '0 auto' }}>
                <div className="card-form-container">
                    <h1 style={{ display: 'flex', alignItems: 'center' }}><Settings size={30} style={{ marginRight: '10px' }} /> Two-Factor Setup</h1>
                    <p style={{ color: '#aaa' }}>Secure your account with an extra layer of protection using an authenticator app (like Google Authenticator).</p>
                    
                    {/* Step 1: Initial Prompt */}
                    {step === 1 && (
                        <div style={{ textAlign: 'center' }}>
                            <p>To begin, click the button below to generate your unique setup key.</p>
                            <button className="btn-primary" onClick={handleGenerate} style={{ marginTop: '20px' }}>
                                <QrCode size={18} style={{ marginRight: '8px' }} /> Generate Setup Key
                            </button>
                        </div>
                    )}

                    {/* Step 2: Display QR and Verify Code */}
                    {step === 2 && (
                        <div style={{ textAlign: 'center', backgroundColor: '#1a1a2e', padding: '30px', borderRadius: '10px' }}>
                            <h3 style={{ color: '#fff' }}>1. Scan the Code</h3>
                            <p style={{ color: '#ccc', marginBottom: '20px' }}>Use your authenticator app to scan the QR code below.</p>
                            
                            <QRCodeDisplay value={qrCodeData} />
                            
                            <h3 style={{ marginTop: '30px', color: '#fff' }}>2. Enter Verification Code</h3>
                            <form onSubmit={handleVerify}>
                                <input 
                                    type="text" 
                                    name="code" 
                                    placeholder="Enter 6-digit code (e.g., 123456)"
                                    value={userInputCode}
                                    onChange={(e) => setUserInputCode(e.target.value)}
                                    maxLength={6}
                                    required
                                    disabled={isVerifying}
                                    style={{ width: '250px', textAlign: 'center', fontSize: '1.2rem' }}
                                />
                                <button type="submit" className="btn-primary" disabled={isVerifying} style={{ marginTop: '20px' }}>
                                    {isVerifying ? 'Verifying...' : 'Activate 2FA'}
                                </button>
                            </form>
                        </div>
                    )}
                    
                    {/* Step 3: Success */}
                    {step === 3 && (
                        <div style={{ textAlign: 'center', backgroundColor: '#215c32', padding: '30px', borderRadius: '10px', color: '#fff' }}>
                            <CheckCircle size={48} style={{ marginBottom: '15px' }} />
                            <h2>Success!</h2>
                            <p>Two-Factor Authentication is now **active** on your account.</p>
                            <p>You will now be prompted for a code every time you log in.</p>
                        </div>
                    )}
                    
                    {statusMessage && (
                        <p style={{ textAlign: 'center', color: step === 3 ? '#2ecc71' : (isVerifying ? '#f1c40f' : '#e74c3c'), marginTop: '20px' }}>
                            {statusMessage}
                        </p>
                    )}
                </div>
            </div>
        </div>
    );
}

export default Setup2FA;