import axios from 'axios';
import { Comment } from './types';

const api = axios.create({
    baseURL: 'http://localhost:8080/api',
    headers: {
        'Content-Type': 'application/json'
    },
    withCredentials: true
});

// 用户注册
export const register = async (user: { username: string; password: string; email: string }) => {
    try {
        const response = await api.post('/auth/register', user);
        return response.data;
    } catch (error) {
        throw error;
    }
};

// 用户登录
export const login = async (usernameOrEmail: string, password: string, rememberMe: boolean) => {
    try {
        const response = await api.post('/auth/login', { usernameOrEmail, password, rememberMe });
        console.log('登录响应数据:', response.data);
        const { accessToken, id, username, email } = response.data;
        const userData = { id, username, email, token: accessToken };

        if (rememberMe) {
            localStorage.setItem('user', JSON.stringify(userData));
        } else {
            sessionStorage.setItem('user', JSON.stringify(userData));
        }

        return userData; // 确保返回完整的用户数据
    } catch (error) {
        console.error('登录错误:', error);
        throw error;
    }
};

// 获取所有留言
export const getAllMessages = async () => {
    try {
        const response = await api.get('/comments');
        return response.data as Comment[];
    } catch (error) {
        throw error;
    }
};

// 发表留言
export const postMessage = async (content: string, parentId?: number) => {
    const user = JSON.parse(localStorage.getItem('user') || sessionStorage.getItem('user') || '{}');
    try {
        const response = await api.post('/comments', {
            content,
            parentId: parentId || null  // 明确传递 parentId 或 null
        }, {
            headers: {
                'Authorization': `Bearer ${user.token}`
            }
        });
        return response.data;
    } catch (error) {
        throw error;
    }
};