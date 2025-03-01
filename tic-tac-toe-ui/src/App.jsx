import { useState, useEffect } from 'react';
import React from 'react';
import { Play, Circle, X } from 'lucide-react';

export default function TicTacToe() {
	const [sessionId, setSessionId] = useState(null);
	const [board, setBoard] = useState([
		['-', '-', '-'],
		['-', '-', '-'],
		['-', '-', '-'],
	]);
	const [status, setStatus] = useState('Waiting to start');

	const startGame = async () => {
		const response = await fetch('http://localhost:8081/sessions', {
			method: 'POST',
		});
		const newSessionId = await response.text();
		setSessionId(newSessionId);
		simulateGame(newSessionId);
	};

	const simulateGame = async (id) => {
		await fetch(`http://localhost:8081/sessions/${id}/simulate`, {
			method: 'POST',
		});
		fetchGameState(id);
	};

	const fetchGameState = async (id) => {
		const response = await fetch(`http://localhost:8081/sessions/${id}`);
		const data = await response.json();
		setStatus(data.status);
		fetchGameBoard(data.gameId);
	};

	const fetchGameBoard = async (gameId) => {
		try {
			const response = await fetch(`http://localhost:8080/games/${gameId}`);
			const gameData = await response.json();
			const formattedBoard = gameData.board.map((row) => row.split(''));
			setBoard(formattedBoard);
			setStatus(gameData.status);
		} catch (error) {
			console.error('Error fetching game board:', error);
		}
	};

	useEffect(() => {
		if (sessionId) {
			const interval = setInterval(() => fetchGameState(sessionId), 1000);
			return () => clearInterval(interval);
		}
	}, [sessionId]);

	return (
		<div className='flex flex-col items-center mt-10'>
			<button
				onClick={startGame}
				className='mb-4 px-6 py-3 bg-blue-500 text-white font-semibold rounded-lg flex items-center gap-2 shadow-lg hover:bg-blue-600 transition-all'
			>
				<Play size={20} /> Start Simulation
			</button>

			<div className='grid grid-cols-3 gap-2 border p-4 bg-gray-100 rounded-lg shadow-lg'>
				{board.map((row, i) =>
					row.map((cell, j) => (
						<div
							key={`${i}-${j}`}
							className='w-20 h-20 flex items-center justify-center border text-3xl font-bold bg-white rounded shadow-sm'
						>
							{cell === '-' ? '' : cell === 'X' ? <X size={48} /> : <Circle size={48} />}
						</div>
					))
				)}
			</div>

			<p className='mt-4 text-lg font-medium bg-gray-200 px-4 py-2 rounded-lg shadow-md'>Status: {status}</p>
		</div>
	);
}
