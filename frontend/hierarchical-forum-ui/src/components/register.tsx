import React, { useState } from 'react';
import { register } from '../api';
import {
    formContainer,
    formTitle,
    inputField,
    submitButton,
    errorMessage
} from '../styles';

const Register: React.FC = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [email, setEmail] = useState('');
    const [error, setError] = useState('');
    const [isLoading, setIsLoading] = useState(false);

    const handleRegister = async () => {
        console.log('注册按钮被点击'); 
        if (!username || !password || !email) {
            setError('请填写所有字段');
            return;
        }

        setIsLoading(true);
        try {
            console.log('发送注册请求', { username, email }); 
            const response = await register({ username, password, email });
            console.log('注册响应', response); 
            setError('');
            alert('注册成功！请登录');
        } catch (error: any) {
            console.error('注册错误:', error); 
            setError(error.response?.data?.message || '注册失败，请重试');
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
            <h2 style={formTitle}>用户注册</h2>

            {error && <div style={errorMessage}>{error}</div>}

            <div style={{ marginBottom: '15px' }}>
                <label style={{ display: 'block', marginBottom: '8px', fontWeight: '500' }}>
                    用户名
                </label>
                <input
                    type="text"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                    style={inputField}
                    placeholder="5-20个字符，字母或数字"
                />
            </div>

            <div style={{ marginBottom: '15px' }}>
                <label style={{ display: 'block', marginBottom: '8px', fontWeight: '500' }}>
                    密码
                </label>
                <input
                    type="password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    style={inputField}
                    placeholder="8-20个字符，包含大小写和特殊符号"
                />
            </div>

            <div style={{ marginBottom: '25px' }}>
                <label style={{ display: 'block', marginBottom: '8px', fontWeight: '500' }}>
                    邮箱
                </label>
                <input
                    type="email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    style={inputField}
                    placeholder="example@domain.com"
                />
            </div>

            <div style={{ flex: '1' }}></div>

            <button
                onClick={handleRegister}
                disabled={isLoading}
                style={{
                    ...submitButton,
                    backgroundColor: isLoading ? '#cccccc' : submitButton.backgroundColor
                }}
            >
                {isLoading ? '注册中...' : '注 册'}
            </button>
        </div>
    );
};

export default Register;