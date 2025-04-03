import React, { useState } from 'react';
import { login } from '../api';
import {
    formContainer,
    formTitle,
    inputField,
    submitButton,
    errorMessage
} from '../styles';

const Login: React.FC<{ onLoginSuccess: (user: any) => void }> = ({ onLoginSuccess }) => {
    const [usernameOrEmail, setUsernameOrEmail] = useState('');
    const [password, setPassword] = useState('');
    const [rememberMe, setRememberMe] = useState(false);
    const [error, setError] = useState('');
    const [isLoading, setIsLoading] = useState(false);

    const handleLogin = async () => {
        if (!usernameOrEmail || !password) {
            setError('请输入用户名/邮箱和密码');
            return;
        }

        setIsLoading(true);
        setError(''); 
        try {
            const user = await login(usernameOrEmail, password, rememberMe);
            onLoginSuccess(user);
        } catch (error: any) {
            console.error('登录请求失败:', error); 
            setError(error.response?.data?.message || '登录失败，请检查凭证');
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div style={{
            ...formContainer,
            flex: '1',
            display: 'flex',
            flexDirection: 'column'
        }}>
            <h2 style={formTitle}>用户登录</h2>

            {error && <div style={errorMessage}>{error}</div>}

            <div style={{ marginBottom: '15px' }}>
                <label style={{ display: 'block', marginBottom: '8px', fontWeight: '500' }}>
                    用户名或邮箱
                </label>
                <input
                    type="text"
                    value={usernameOrEmail}
                    onChange={(e) => setUsernameOrEmail(e.target.value)}
                    style={inputField}
                    onKeyDown={(e) => e.key === 'Enter' && handleLogin()} 
                />
            </div>

            <div style={{ marginBottom: '20px' }}>
                <label style={{ display: 'block', marginBottom: '8px', fontWeight: '500' }}>
                    密码
                </label>
                <input
                    type="password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    style={inputField}
                    onKeyDown={(e) => e.key === 'Enter' && handleLogin()} 
                />
            </div>

            <div style={{
                display: 'flex',
                alignItems: 'flex-start', 
                margin: '10px 0 25px 0', 
                paddingTop: '8px' 
            }}>
                <input
                    type="checkbox"
                    id="rememberMe"
                    checked={rememberMe}
                    onChange={(e) => setRememberMe(e.target.checked)}
                    style={{
                        marginRight: '10px',
                        width: '18px',
                        height: '18px',
                        marginTop: '3px'
                    }}
                />
                <label htmlFor="rememberMe" style={{ cursor: 'pointer', lineHeight: '1.5' }}>
                    记住我
                </label>
            </div>

            <div style={{ flex: '1' }}></div>

            <button
                onClick={handleLogin}
                disabled={isLoading}
                style={{
                    ...submitButton,
                    opacity: isLoading ? 0.8 : 1 
                }}
            >
                {isLoading ? '登录中...' : '登 录'}
            </button>
        </div>
    );
};

export default Login;